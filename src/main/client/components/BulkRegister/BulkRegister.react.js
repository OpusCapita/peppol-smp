import React from 'react';
import {Components} from '@opuscapita/service-base-ui';
import {ApiBase} from '../../api';
import Select from '@opuscapita/react-select';
import './BulkRegister.css';

class BulkRegister extends Components.ContextComponent {

    state = {
        loading: false,
        showOther: false,
        showDocumentTypes: false,
        documentTypes: [],
        selectedDocumentTypes: [],
        participantList: []
    };

    constructor(props, context) {
        super(props);
        this.api = new ApiBase();
    }

    async componentDidMount() {
        await this.fetchDocumentTypesFromValidator();
    }

    async fetchDocumentTypesFromValidator() {
        try {
            const documentTypes = await this.api.getDocumentTypes();
            const filteredDocumentTypes = documentTypes.filter(d => {
                return ["PEPPOL_BIS30", "SVE", "BEAst"].includes(d.archetype);
            });
            filteredDocumentTypes.forEach(d => {
                d.value = d.id;
                d.label = "[" + d.id + "] " + d.description;
            });
            this.setState({documentTypes: filteredDocumentTypes});
        } catch (e) {
            this.context.showNotification(e.message, 'error', 10);
        }
    }

    handleDocumentTypesChange(value) {
        this.setState({selectedDocumentTypes: value});
    }

    handleProfileChange(e) {
        const item = e.target.name;
        const isChecked = e.target.checked;
        const documentTypes = this.state.documentTypes;
        const preDocumentTypes = this.state.selectedDocumentTypes || [];

        const pushIfNotExist = function (arr, items) {
            const temp = arr.slice();
            for (let i = 0; i < items.length; i++) {
                if (!temp.some(d => d.id === items[i].id)) {
                    temp.push(items[i]);
                }
            }
            return temp;
        };

        let finalDocumentTypes;
        if (item === "bis3") {
            if (isChecked) {
                finalDocumentTypes = pushIfNotExist(preDocumentTypes, documentTypes.filter(d => [158, 160].includes(d.id)));
            } else {
                finalDocumentTypes = preDocumentTypes.filter(d => ![158, 160].includes(d.id))
            }
        } else if (item === "bis3-1") {
            if (isChecked) {
                finalDocumentTypes = pushIfNotExist(preDocumentTypes, documentTypes.filter(d => d.archetype === 'PEPPOL_BIS30' && ![158, 160].includes(d.id)));
            } else {
                finalDocumentTypes = preDocumentTypes.filter(d => d.archetype !== 'PEPPOL_BIS30' || [158, 160].includes(d.id));
            }
        } else if (item === "sve") {
            if (isChecked) {
                finalDocumentTypes = pushIfNotExist(preDocumentTypes, documentTypes.filter(d => d.archetype === 'SVE'));
            } else {
                finalDocumentTypes = preDocumentTypes.filter(d => d.archetype !== 'SVE');
            }
        } else if (item === "beast") {
            if (isChecked) {
                finalDocumentTypes = pushIfNotExist(preDocumentTypes, documentTypes.filter(d => d.archetype === 'BEAst'));
            } else {
                finalDocumentTypes = preDocumentTypes.filter(d => d.archetype !== 'BEAst');
            }
        } else if (item === "other") {
            this.setState({showOther: isChecked});
            return;
        }

        this.handleDocumentTypesChange(finalDocumentTypes);
    }

    loadFile(event) {
        const file = event.target.files[0];
        if (!file) {
            return;
        }

        const reader = new FileReader();
        reader.onload = (e) => {
            const raw = e.target.result;
            if (raw) {
                const participantList = [];

                const rows = raw.split("\n");
                for (let i = 1; i < rows.length; i++) {
                    const columns = rows[i].split(",");

                    const participant = {};
                    participant.name = columns[0];
                    participant.icd = columns[1].split(":")[0];
                    participant.identifier = columns[1].split(":")[1];
                    participant.contactName = columns[2];
                    participant.contactEmail = columns[3];
                    participant.contactPhone = columns[4];
                    participant.endpointType = "TEST";
                    participantList.push(participant);
                }
                this.setState({participantList, showDocumentTypes: true});
                this.context.showNotification(participantList.length + ' participants loaded', 'info', 3);
            }
        };
        reader.readAsText(file);
    }

    handleCancel(event) {
        event && event.preventDefault();
        this.context.router.push(`/peppol-smp`);
    }

    async bulkRegister() {
        const {participantList, selectedDocumentTypes} = this.state;
        const {userData, showModalDialog, hideModalDialog} = this.context;

        const onConfirmationClick = (btn) => {
            hideModalDialog();

            if (btn === 'yes') {
                this.context.showSpinner();

                setTimeout(() => {
                    this.api.bulkRegister({participants: participantList, documentTypes: selectedDocumentTypes.map(d => { return {internalId: d.id}; })}, userData.id).then(() => {
                        this.context.showNotification('Bulk register operation has been successfully initialized', 'info', 3);

                    }).catch(e => {
                        this.context.showNotification(e.message, 'error', 10);

                    }).finally(() => {
                        this.context.hideSpinner();
                    });

                }, 500);
            }
        };

        const modalTitle = "Bulk Register";
        const modalText = `${participantList.length} participants will be registered in the background.\n\nDo you want to continue?`;
        const modalButtons = {no: 'No', yes: 'Yes'};

        showModalDialog(modalTitle, modalText, onConfirmationClick, modalButtons);
    }

    render() {
        const {participantList, documentTypes, selectedDocumentTypes, showDocumentTypes, showOther} = this.state;
        return (
            <div>
                <h2>Bulk Register</h2>
                <label className="btn btn-default upload-btn">
                    Select the file to load the participants
                    <div className="upload-btn-explanation">
                        (You need to select a cvs file with participant details, one each line)
                    </div>
                    <input type="file" hidden onChange={e => this.loadFile(e)}/>
                </label>

                {
                    showDocumentTypes &&
                    <div className="form-horizontal participant-form">
                        <div className="row">
                            <div className="col-md-12">
                                <div className="form-group">
                                    <div className="col-sm-3">&nbsp;</div>
                                    <div className="offset-md-1 col-md-8">
                                        <label className="label-explanation">
                                            <strong>{participantList.length}</strong> participants loaded, now select their supported document types.
                                        </label>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">&nbsp;</div>
                                    <div className="offset-md-1 col-md-8">
                                        <label className="container">PEPPOL BIS Billing v3.0 (Invoice and CreditNote)
                                            <input type="checkbox" name="bis3"
                                                   onChange={e => this.handleProfileChange(e)}/>
                                            <span className="checkmark"/>
                                        </label>
                                        <label className="container">PEPPOL BIS Poacc Upgrade v3.1 (Order, Catalogue,
                                            Order
                                            Response, Invoice Response...)
                                            <input type="checkbox" name="bis3-1"
                                                   onChange={e => this.handleProfileChange(e)}/>
                                            <span className="checkmark"/>
                                        </label>
                                        <label className="container">SVEFaktura (Svefaktura v1 Invoice,
                                            Invoice+Envelope,
                                            SFTI Svekatalog v2, SVE Order)
                                            <input type="checkbox" name="sve"
                                                   onChange={e => this.handleProfileChange(e)}/>
                                            <span className="checkmark"/>
                                        </label>
                                        <label className="container">BEAst v3.0.1 (Invoic, Order, OrderChange,
                                            OrderResponse, DespatchAdvice)
                                            <input type="checkbox" name="beast"
                                                   onChange={e => this.handleProfileChange(e)}/>
                                            <span className="checkmark"/>
                                        </label>
                                        <label className="container">Add document types one by one...
                                            <input type="checkbox" name="other" checked={showOther}
                                                   onChange={e => this.handleProfileChange(e)}/>
                                            <span className="checkmark"/>
                                        </label>
                                    </div>
                                </div>
                                {
                                    showOther &&
                                    <div className="form-group">
                                        <div className="col-sm-3">&nbsp;</div>
                                        <div className="offset-md-1 col-md-8">
                                            <Select className="react-select" isMulti={true}
                                                    options={documentTypes}
                                                    value={selectedDocumentTypes}
                                                    onChange={value => this.handleDocumentTypesChange(value)}
                                            />
                                        </div>
                                    </div>
                                }
                            </div>
                        </div>
                    </div>
                }

                <div className="form-submit text-right advanced-actions">
                    <button className="btn btn-link" onClick={e => this.handleCancel(e)}>Cancel</button>
                    <button className="btn btn-success" onClick={(e) => this.bulkRegister(e)}>Register</button>
                    <button className="btn btn-info float-left" onClick={() => window.open("https://github.com/OpusCapita/peppol-smp/wiki/Bulk-Register", "_blank")}>
                        Help?
                    </button>
                </div>
            </div>
        );
    }
}

export default BulkRegister;

import React from 'react';
import {Components} from '@opuscapita/service-base-ui';
import {ApiBase} from '../../api';
import './BulkRegister.css';

class BulkRegister extends Components.ContextComponent {

    state = {
        loading: false,
        rawFile: null,
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
        await this.fetchParticipantToEdit();
        await this.fetchDocumentTypesFromValidator();
    }

    async fetchDocumentTypesFromValidator() {
        try {
            const documentTypes = await this.api.getDocumentTypes();
            const filteredDocumentTypes = documentTypes.filter(d => {
                return ["PEPPOL_BIS30", "EHF", "SVE", "BEAst"].includes(d.archetype);
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
        } else if (item === "ehf") {
            if (isChecked) {
                finalDocumentTypes = pushIfNotExist(preDocumentTypes, documentTypes.filter(d => d.archetype === 'EHF'));
            } else {
                finalDocumentTypes = preDocumentTypes.filter(d => d.archetype !== 'EHF');
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
                this.setState({rawFile: file});
                this.setState({participantList: raw.split("\n")});
                this.context.showNotification(this.state.participantList.length + ' participants loaded', 'info', 3);
            }
        };
        reader.readAsText(file);
    }

    async bulkRegister() {
        const {participantList, selectedDocumentTypes} = this.state;
        const {userData, showModalDialog, hideModalDialog} = this.context;

        const onConfirmationClick = (btn) => {
            hideModalDialog();

            if (btn === 'yes') {
                console.log("participantList: ");
                console.log(participantList);
                console.log("selectedDocumentTypes: ");
                console.log(selectedDocumentTypes);

                // this.context.showSpinner();
                //
                // setTimeout(() => {
                //     this.api.reprocessMessagesAdvanced(transmissionList, userData.id).then(() => {
                //         this.context.showNotification('Reprocessing of the messages has been started', 'info', 3);
                //
                //     }).catch(e => {
                //         this.context.showNotification(e.message, 'error', 10);
                //
                //     }).finally(() => {
                //         this.context.hideSpinner();
                //     });
                //
                // }, 500);
            }
        };

        const modalTitle = "Bulk Register";
        const modalText = `${participantList.length} participants will be registered in the background.\n\nDo you want to continue?`;
        const modalButtons = {no: 'No', yes: 'Yes'};

        showModalDialog(modalTitle, modalText, onConfirmationClick, modalButtons);
    }

    render() {
        const {documentTypes, selectedDocumentTypes, showDocumentTypes, showOther} = this.state;
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
                                    <div className="col-sm-3">
                                        <label className="control-label btn-link">Supported Profiles</label>
                                    </div>
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
                                        <label className="container">EHF v2 (Invoice, CreditNote, Catalogue, Order,
                                            OrderResponse...)
                                            <input type="checkbox" name="ehf"
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
                                        <label className="container">Custom...
                                            <input type="checkbox" name="other" checked={showOther}
                                                   onChange={e => this.handleProfileChange(e)}/>
                                            <span className="checkmark"/>
                                        </label>
                                    </div>
                                </div>
                                {
                                    showOther &&
                                    <div className="form-group">
                                        <div className="col-sm-3">
                                            <label className="control-label btn-link">Supported Documents</label>
                                        </div>
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
                    <button className="btn btn-default" onClick={() => this.bulkRegister()}>Register</button>
                </div>
            </div>
        );
    }
}

export default BulkRegister;

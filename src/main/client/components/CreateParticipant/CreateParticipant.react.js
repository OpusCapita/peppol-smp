import React from 'react';
import {ApiBase, Countries, IcdValues} from '../../api';
import {Components} from '@opuscapita/service-base-ui';
import Select from '@opuscapita/react-select';
import './CreateParticipant.css';

class CreateParticipant extends Components.ContextComponent {

    state = {
        participant: {
            icd: IcdValues[5],
            identifier: "999666333",
            name: "Test Org",
            country: Countries[5],
            contactInfo: "Test Contactinf",
            documentTypes: []
        },
        documentTypes: [],
        showOther: false,
    };

    constructor(props, context) {
        super(props);
        this.api = new ApiBase();

        this.handleFormChange = this.handleFormChange.bind(this);
        this.handleProfileChange = this.handleProfileChange.bind(this);
    }

    async componentDidMount() {
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
            this.setState({documentTypes: []});
            this.context.showNotification(e.message, 'error', 10);
        }
    }

    handleFormChange(field, value) {
        const {participant} = this.state;
        participant[field] = value;
        this.setState({participant});
    }

    mapIcdValuesSelect() {
        return IcdValues.map(value => {
            return {value: value.icd, label: `${value.icd} - ${value.code} (${value.name})`};
        });
    }

    mapCountriesSelect() {
        return Countries.map(value => {
            return {value: value.code, label: value.name};
        });
    }

    handleProfileChange(e) {
        const item = e.target.name;
        const isChecked = e.target.checked;
        const documentTypes = this.state.documentTypes;
        const preDocumentTypes = this.state.participant.documentTypes || [];

        const pushIfNotExist = function (arr, items) {
            for (let i = 0; i < items.length; i++) {
                let item = items[i];
                if (!arr.some(d => d.id === item.id)) {
                    arr.push(item);
                }
            }
            return arr;
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

        this.handleFormChange('documentTypes', finalDocumentTypes);
    }

    handleCancel(event) {
        event && event.preventDefault();
        this.context.router.push(`/peppol-smp`);
    }

    handleSubmit(event) {
        //this.context.showSpinner();

        const {participant} = this.state;
        participant.icd = participant.icd.value;
        participant.country = participant.country.value;
        participant.documentTypes = participant.documentTypes.map(d => {
            const temp = {};
            temp.internalId = d.id;
            return temp;
        });

        console.log(participant); return;

        this.api.addParticipant(this.state.participant).then(response => {
            this.context.hideSpinner();
            this.setState({documentTypes: []});
            this.context.showNotification('The participant is registered successfully', 'success', 10);

        }).catch(e => {
            this.context.hideSpinner();
            this.context.showNotification(e.message, 'error', 10);
        });
    }

    render() {
        const {participant, documentTypes, showOther} = this.state;
        return (
            <div>
                <h3>Register New Participant</h3>
                <div className="form-horizontal participant-form">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Organization Identifier</label>
                                </div>
                                <div className="offset-md-1 col-md-4">
                                    <Select className="react-select" isMulti={false}
                                            value={participant.icd}
                                            options={this.mapIcdValuesSelect()}
                                            onChange={value => this.handleFormChange('icd', value)}
                                    />
                                </div>
                                <div className="offset-md-1 col-md-4">
                                    <input type="text" className="form-control" value={participant.identifier}
                                           onChange={e => this.handleFormChange('identifier', e.target.value)}
                                           placeholder="Participant Identifier"
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Organization Name</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <input type="text" className="form-control" value={participant.name}
                                           onChange={e => this.handleFormChange('name', e.target.value)}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Organization Country</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <Select className="react-select" isMulti={false}
                                            value={participant.country}
                                            options={this.mapCountriesSelect()}
                                            onChange={value => this.handleFormChange('country', value)}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Contact Info</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <input type="text" className="form-control" value={participant.contactInfo}
                                           onChange={e => this.handleFormChange('contactInfo', e.target.value)}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Supported Profiles</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="container">PEPPOL BIS Billing v3.0 (Invoice and CreditNote)
                                        <input type="checkbox" name="bis3" onChange={e => this.handleProfileChange(e)}/>
                                        <span className="checkmark"/>
                                    </label>
                                    <label className="container">PEPPOL BIS Poacc Upgrade v3.1 (Order, Catalogue, Order
                                        Response, Invoice Response...)
                                        <input type="checkbox" name="bis3-1"
                                               onChange={e => this.handleProfileChange(e)}/>
                                        <span className="checkmark"/>
                                    </label>
                                    <label className="container">EHF v2 (Invoice, CreditNote, Catalogue, Order,
                                        OrderResponse...)
                                        <input type="checkbox" name="ehf" onChange={e => this.handleProfileChange(e)}/>
                                        <span className="checkmark"/>
                                    </label>
                                    <label className="container">SVEFaktura (Svefaktura v1 Invoice, Invoice+Envelope,
                                        SFTI Svekatalog v2, SVE Order)
                                        <input type="checkbox" name="sve" onChange={e => this.handleProfileChange(e)}/>
                                        <span className="checkmark"/>
                                    </label>
                                    <label className="container">BEAst v3.0.1 (Invoic, Order, OrderChange,
                                        OrderResponse, DespatchAdvice)
                                        <input type="checkbox" name="beast"
                                               onChange={e => this.handleProfileChange(e)}/>
                                        <span className="checkmark"/>
                                    </label>
                                    <label className="container">Custom...
                                        <input type="checkbox" name="other"
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
                                                value={participant.documentTypes}
                                                onChange={value => this.handleFormChange('documentTypes', value)}
                                        />
                                    </div>
                                </div>
                            }
                        </div>
                    </div>

                </div>
                <div className="form-submit text-right participant-form-actions">
                    <button className="btn btn-link" onClick={e => this.handleCancel(e)}>Cancel</button>
                    <button className="btn btn-success" onClick={e => this.handleSubmit(e)}>Register</button>
                </div>
            </div>
        );
    }
}

export default CreateParticipant;

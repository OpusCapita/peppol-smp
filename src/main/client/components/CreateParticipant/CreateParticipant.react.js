import React from 'react';
import PropTypes from 'prop-types';
import {ApiBase, Countries, IcdValues} from '../../api';
import {Components} from '@opuscapita/service-base-ui';
import Select from '@opuscapita/react-select';
import './CreateParticipant.css';

class CreateParticipant extends Components.ContextComponent {

    state = {
        participant: {
            endpointType: "TEST",
            documentTypes: []
        },
        documentTypes: [],
        showOther: false,
        editMode: false,
    };

    static propTypes = {
        id: PropTypes.number
    };

    constructor(props, context) {
        super(props);
        this.api = new ApiBase();

        this.handleCancel = this.handleCancel.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleFormChange = this.handleFormChange.bind(this);
        this.handleProfileChange = this.handleProfileChange.bind(this);
    }

    async componentDidMount() {
        await this.fetchDocumentTypesFromValidator();
        await this.fetchParticipantToEdit();
    }

    async fetchParticipantToEdit() {
        if (!this.props.id) {
            return;
        }

        const participant = await this.api.getParticipantDetailById(this.props.id);

        const icdValue = IcdValues.findByIcd(participant.icd);
        participant.icd = {value: icdValue.icd, label: `${icdValue.icd} - ${icdValue.code}`};
        const countryValue = Countries.findByCode(participant.country);
        participant.country = !!countryValue ?  {value: countryValue.code, label: countryValue.name} : countryValue;

        const documentTypes = this.state.documentTypes;
        if (documentTypes && documentTypes.length) {
            participant.documentTypes = participant.documentTypes.map(d => documentTypes.find(i => i.id === d.internalId)).filter(d => !!d);
        }
        this.setState({participant, editMode: true, showOther: true});
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

    mapIcdValuesSelect() {
        return IcdValues.map(value => {
            return {value: value.icd, label: `${value.icd} - ${value.code}`};
        });
    }

    mapCountriesSelect() {
        return Countries.map(value => {
            return {value: value.code, label: value.name};
        });
    }

    handleFormChange(field, value) {
        const {participant} = this.state;
        participant[field] = value;
        this.setState({participant});
    }

    handleProfileChange(e) {
        const item = e.target.name;
        const isChecked = e.target.checked;
        const documentTypes = this.state.documentTypes;
        const preDocumentTypes = this.state.participant.documentTypes || [];

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

        this.handleFormChange('documentTypes', finalDocumentTypes);
    }

    handleReset() {
        this.setState({participant: {endpointType: this.state.participant.endpointType, documentTypes: []}});
    }

    handleCancel(event) {
        event && event.preventDefault();
        this.context.router.push(`/peppol-smp`);
    }

    handleSubmit(event) {
        this.context.showSpinner();

        const {participant} = this.state;
        participant.icd = participant.icd.value;
        participant.country = participant.country.value;
        participant.documentTypes = participant.documentTypes.map(d => {
            const temp = {};
            temp.internalId = d.id;
            return temp;
        });

        this.api.addParticipant(participant, this.context.userData.id).then(response => {
            this.handleReset();
            this.context.showNotification('The participant is registered successfully', 'success', 10);
        }).catch(e => {
            this.context.showNotification(e.message, 'error', 10);
        }).finally(() => {
            this.context.hideSpinner();
        });
    }

    render() {
        const {participant, documentTypes, showOther, editMode} = this.state;
        return (
            <div>
                <h3>{editMode ? "Update" : "Register"} Participant</h3>
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
                                    <label className="control-label btn-link">Contact Name</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <input type="text" className="form-control" value={participant.contactName}
                                           onChange={e => this.handleFormChange('contactName', e.target.value)}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Contact E-Mail</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <input type="text" className="form-control" value={participant.contactEmail}
                                           onChange={e => this.handleFormChange('contactEmail', e.target.value)}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Contact Phone</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <input type="text" className="form-control" value={participant.contactPhone}
                                           onChange={e => this.handleFormChange('contactPhone', e.target.value)}
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
                    <button className="btn btn-success" onClick={e => this.handleSubmit(e)}>{editMode ? "Update" : "Register"}</button>
                </div>
            </div>
        );
    }
}

export default CreateParticipant;

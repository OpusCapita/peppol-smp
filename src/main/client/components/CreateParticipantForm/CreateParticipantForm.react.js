import React from 'react';
import {ApiBase, Countries} from '../../api';
import {Components} from '@opuscapita/service-base-ui';
import Select from '@opuscapita/react-select';
import './CreateParticipantForm.css';

class CreateParticipantForm extends Components.ContextComponent {

    state = {
        participant: {},
        documentTypes: []
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
            documentTypes.forEach(d => {
                d.value = d.id;
                d.label = "[" + d.id + "] " + d.description;
            });
            this.setState({documentTypes: documentTypes});
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

    mapCountriesSelect() {
        return Countries.map(value => {
            return {value: value.code, label: value.name};
        });
    }

    mapDocumentTypesSelect() {
        return this.state.documentTypes.map(value => {
            return {value: value.id, label: value.name};
        });
    }

    resetState() {
        this.setState({participant: {}, documentTypes: []});
    }

    handleCancel(event) {
        event && event.preventDefault();
        this.context.router.push(`/peppol-smp/participants`);
    }

    handleSubmit(event) {
        this.context.showSpinner();

        const {participant} = this.state;
        participant.country = participant.country.value;
        participant.documentTypes = participant.documentTypes.map(d => d.value);

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
        const {participant, documentTypes} = this.state;
        return (
            <div>
                <h3>Create New Participant</h3>
                <div className="form-horizontal participant-form">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Organization Identifier</label>
                                </div>
                                <div className="offset-md-1 col-md-3">
                                    <input type="text" className="form-control" value={participant.icd}
                                           onChange={e => this.handleFormChange('icd', e.target.value)}
                                           placeholder="ISO 6523 Identifier"
                                    />
                                </div>
                                <div className="offset-md-1 col-md-5">
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
                                    <label className="control-label btn-link">Supported Documents</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <Select className="react-select" isMulti={true}
                                            options={documentTypes}
                                            onChange={value => this.handleFormChange('documentTypes', value)}
                                            value={participant.documentTypes}
                                    />
                                </div>
                            </div>
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

export default CreateParticipantForm;

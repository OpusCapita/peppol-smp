import React from 'react';
import {ApiBase} from '../../api';
import {Countries} from '../../api/Countries';
import {Components} from '@opuscapita/service-base-ui';
import './CreateParticipantForm.css';

class CreateParticipantForm extends Components.ContextComponent {

    state = {
        continued: false,
        participant: {},
        documentTypes: []
    };

    constructor(props, context) {
        super(props);

        this.api = new ApiBase();
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

    loadParticipant(event) {
        event && event.preventDefault();
        if (!this.state.participant.icd || !this.state.participant.identifier) {
            this.context.showNotification('Enter icd number and participant identifier first.', 'info', 3);
            return;
        }

        // load document types here
        this.setState({continued: true});
    }

    render() {
        const {continued, participant} = this.state;
        return (
            <div>
                <h3>Create New Participant</h3>
                <div className="form-horizontal participant-form">
                    <div className="flex-box">
                        <div className="flex-item-5">
                            <input type="text" placeholder="ISO 6523 identifier" className="form-control"
                                   value={participant.icd} onChange={e => this.handleFormChange('icd', e.target.value)}/>
                        </div>
                        <div className="flex-item-5">
                            <input type="text" placeholder="Participant identifier" className="form-control"
                                   value={participant.identifier} onChange={e => this.handleFormChange('identifier', e.target.value)}/>
                        </div>
                        <div className="flex-item-2">
                            <button className="btn btn-primary" onClick={(e) => this.loadParticipant(e)}>Continue</button>
                        </div>
                    </div>
                    {
                        continued &&
                        <div className="row">
                            <div className="col-md-12">
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
                                                options={this.mapDocumentTypesSelect()}
                                                onChange={value => this.handleFormChange('documentTypes', value)}
                                                value={participant.documentTypes && participant.documentTypes.map(src => ({
                                                    label: src,
                                                    value: src
                                                }))}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    }
                </div>
            </div>
        );
    }
}

export default CreateParticipantForm;

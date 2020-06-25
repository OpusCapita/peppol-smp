import React from 'react';
import PropTypes from 'prop-types';
import {Components} from '@opuscapita/service-base-ui';
import {ApiBase, Countries, IcdValues} from '../../api';
import {Consumer} from "../../api/DocumentTypes";
import 'react-table/react-table.css';
import './LookupParticipant.css';
import Select from "@opuscapita/react-select";

class LookupParticipant extends Components.ContextComponent {

    state = {
        icd: '',
        identifier: ''
    };

    static propTypes = {
        icd: PropTypes.string,
        identifier: PropTypes.string,
    };

    constructor(props, context) {
        super(props);
        this.api = new ApiBase();
    }

    componentDidMount() {
        if (this.props.icd && this.props.identifier) {
            const icdObj = IcdValues.findByIcd(this.props.icd);
            this.setState({
                icd: {value: icdObj.icd, label: `${icdObj.icd} - ${icdObj.code}`},
                identifier: this.props.identifier
            });
            this.handleSubmit();
        }
    }

    getDocumentTypes(documentTypes) {
        const {participant} = this.state;
        return participant.documentTypes.map((ext, inx) => {
            ext.id = inx + 1;
            if (ext.internalId != null) {
                ext.name = documentTypes.filter(int => int.id === ext.internalId)[0].description;
            }
            return ext;
        });
    }

    mapIcdValuesSelect() {
        return IcdValues.map(value => {
            return {value: value.icd, label: `${value.icd} - ${value.code}`};
        });
    }

    handleClean(event) {
        event && event.preventDefault();
        this.setState({icd: '', identifier: '', response: null});
    }

    handleSubmit(event) {
        event && event.preventDefault();
        const {icd, identifier} = this.state;
        const {showNotification, showSpinner, hideSpinner} = this.context;

        if (!icd || !identifier) {
            showNotification("Please provide valid ICD and identifier values.", "error", 3);
        }

        showSpinner();
        this.api.lookupParticipant(icd.value, identifier).then((response) => {
            hideSpinner();

            if (response.errorMessage) {
                showNotification(response.errorMessage, 'error', 5);
            } else {
                this.setState({response: response});
            }
        }).catch(e => {
            hideSpinner();
            showNotification(e.message, 'error', 10);
        });
    }

    render() {
        const {icd, identifier, response} = this.state;
        return (
            <div>
                <h3>Participant Lookup</h3>
                <div className="form-horizontal participant-form">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Organization Identifier</label>
                                </div>
                                <div className="offset-md-1 col-md-4">
                                    <Select className="react-select" isMulti={false}
                                            value={icd}
                                            options={this.mapIcdValuesSelect()}
                                            onChange={value => this.setState({icd: value})}
                                    />
                                </div>
                                <div className="offset-md-1 col-md-4">
                                    <input type="text" className="form-control" value={identifier}
                                           onChange={e => this.setState({identifier: e.target.value})}
                                           placeholder="Participant Identifier"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                {
                    response &&
                    this.renderLookupResult(response)
                }
                <div className="form-submit text-right participant-form-actions">
                    <button className="btn btn-link" onClick={e => this.handleClean(e)}>Clean</button>
                    <button className="btn btn-success" onClick={e => this.handleSubmit(e)}>Lookup</button>
                </div>
            </div>
        );
    }

    renderLookupResult(response) {
        return <div className="form-horizontal participant-detail">
            <div className="row">
                <div className="col-md-12">
                    <div className="form-group">
                        <div className="col-sm-3">
                            <label className="control-label btn-link">Name</label>
                        </div>
                        <div className="offset-md-1 col-md-8">
                            <label className="control-label">{response.name}</label>
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="col-sm-3">
                            <label className="control-label btn-link">Contact Name</label>
                        </div>
                        <div className="offset-md-1 col-md-8">
                            <label className="control-label">{response.contactName}</label>
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="col-sm-3">
                            <label className="control-label btn-link">Contact Email</label>
                        </div>
                        <div className="offset-md-1 col-md-8">
                            <label className="control-label">{response.contactEmail}</label>
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="col-sm-3">
                            <label className="control-label btn-link">Contact Phone</label>
                        </div>
                        <div className="offset-md-1 col-md-8">
                            <label className="control-label">{response.contactPhone}</label>
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="col-sm-3">
                            <label className="control-label btn-link">Supported Profiles</label>
                        </div>
                        <div className="offset-md-1 col-md-8">
                            <Consumer>
                                {({ documentTypes }) => (
                                    this.renderSupportedDocumentTypes(response.documentTypeList, documentTypes)
                                )}
                            </Consumer>
                        </div>
                    </div>
                </div>
            </div>
        </div>;
    }

    renderSupportedDocumentTypes(documentTypeList, ourDocumentTypes) {
        return documentTypeList.map(documentType =>
            <label className="container">
                {(ourDocumentTypes.find(d => d.documentId === documentType.documentTypeIdentifier.identifier && d.processId === documentType.processIdentifier.identifier) || {description: "Unknown"}).description}
                <input type="checkbox" checked={true}/><span className="checkmark"/>
            </label>
        );
    }
}

export default LookupParticipant;

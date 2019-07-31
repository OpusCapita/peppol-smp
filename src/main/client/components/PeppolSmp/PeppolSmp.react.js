import React from 'react';
import {ApiBase} from '../../api';
import {Components} from '@opuscapita/service-base-ui';
import './PeppolSmp.css';

class PeppolSmp extends Components.ContextComponent {

    state = {
        icd: '',
        identifier: '',
        result: {participants: []}
    };

    constructor(props, context) {
        super(props);

        this.api = new ApiBase();
    }

    componentDidMount() {
        const page = this.context.router.location.query.r;
        const participant = this.context.router.location.query.q;

        if (page) {
            this.showPage(page)
        }

        if (participant) {
            this.setState({icd: participant.split(':')[0], identifier: participant.split(':')[1]});
            this.lookup();
        }
    }

    showPage(page, event) {
        event && event.preventDefault();
        this.context.router.push(`/peppol-smp/${page}`);
    }

    handleFormChange(field, value) {
        const searchValues = {};
        searchValues[field] = value;
        this.setState(searchValues);
    }

    lookup(event) {
        event && event.preventDefault();
        if (!this.state.icd || !this.state.identifier) {
            if (event) {
                this.context.showNotification('Enter icd number and participant identifier first.', 'info', 3);
            }
            return;
        }

        this.context.showSpinner();
        this.api.getLookup(this.state.icd, this.state.identifier).then((response) => {
            this.context.hideSpinner();
            this.setState({result: response});
        }).catch(e => {
            this.context.hideSpinner();
            this.context.showNotification(e.message, 'error', 10);
        });
    }

    renderDocumentTypes(documentTypes) {
        if (!documentTypes || !documentTypes.length) {
            return <label className="control-label">Does not support any document types.</label>
        }

        const list = [];
        for (var i = 0; i < documentTypes.length; i++) {
            let d = documentTypes[i];

            list.push(<div className={`document-type-wrapper ${i % 2 ? 'odd' : 'even'}`}>
                <label className="control-label">{d.commonName}
                    <a href={d.url} target="_blank">
                        <span className="glyphicon glyphicon-info-sign document-more-icon"></span>
                    </a>
                </label>
                <label className="control-label small">
                    <li>{d.processIdentifier}</li>
                </label>
                <label className="control-label small">
                    <li>{d.documentIdentifier}</li>
                </label>
            </div>);
        }

        return list;
    }

    renderParticipants(result) {
        if (!result || !result.participants || !result.participants.length) {
            return <h3 className="lookup-result-header"><span>Not Found!</span></h3>
        }

        const list = [];
        result.participants.forEach((p) => {
            list.push(<div>
                <h3 className="lookup-result-header"><span>{p.icd}:{p.identifier}</span></h3>
                <div className="lookup-result row">
                    <div className="col-md-12">
                        <div className="form-group">
                            <div className="col-sm-3">
                                <label className="control-label btn-link">Access Point</label>
                            </div>
                            <div className="col-sm-9">
                                <label className="control-label">{p.apDescription}</label>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-3">
                                <label className="control-label btn-link">Transport Protocol</label>
                            </div>
                            <div className="col-sm-9">
                                <label className="control-label">{p.apProtocol}</label>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-3">
                                <label className="control-label btn-link">Access Point URL</label>
                            </div>
                            <div className="col-sm-9">
                                <label
                                    className="control-label">{p.apUrl}</label>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-3">
                                <label className="control-label btn-link">Access Point Certificate</label>
                            </div>
                            <div className="col-sm-9">
                                <label className="control-label">{p.apCertificate}</label>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-3">
                                <label className="control-label btn-link">Contact</label>
                            </div>
                            <div className="col-sm-9">
                                <label className="control-label">{p.contact}</label>
                            </div>
                        </div>
                        <hr className="inner-line"/>
                        <div className="form-group">
                            <div className="col-sm-3"><label className="control-label btn-link">Supported Documents</label></div>
                            <div className="col-sm-9">
                                {this.renderDocumentTypes(p.documentTypes)}
                            </div>
                        </div>
                    </div>
                </div>
            </div>);
        });

        return list;
    }

    render() {
        const {icd, identifier, result} = this.state;
        return (
            <div>
                <h2>
                    PEPPOL Participant Management
                    <button className="btn btn-info participant-list-btn" onClick={(e) => this.showPage('participants', e)}>
                        Participant List
                    </button>
                </h2>
                <div className="form-horizontal smp-home">
                    <div className="flex-box">
                        <div className="flex-item-5">
                            <input type="text" placeholder="ISO 6523 identifier" className="form-control"
                                   value={icd} onChange={e => this.handleFormChange('icd', e.target.value)}/>
                        </div>
                        <div className="flex-item-5">
                            <input type="text" placeholder="Participant identifier" className="form-control"
                                   value={identifier} onChange={e => this.handleFormChange('identifier', e.target.value)}/>
                        </div>
                        <div className="flex-item-2">
                            <button className="btn btn-primary" onClick={(e) => this.lookup(e)}>Search</button>
                        </div>
                    </div>
                    {this.renderParticipants(result)}
                </div>
            </div>
        );
    }
}

export default PeppolSmp;

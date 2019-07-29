import React from 'react';
import {ApiBase} from '../../api';
import {Components} from '@opuscapita/service-base-ui';
import './PeppolSmp.css';

class PeppolSmp extends Components.ContextComponent {

    state = {
        loading: false,
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

        if (page) {
            this.showPage(page)
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

    lookup() {
        if (!this.state.icd || !this.state.identifier) {
            this.context.showNotification('Enter icd number and participant identifier first.', 'info', 3);
            return;
        }

        this.setState({loading: true});
        this.api.getLookup(this.state.icd, this.state.identifier).then((response) => {
            this.setState({loading: false, result: response});
        }).catch(e => {
            this.setState({loading: false});
            this.context.showNotification(e.message, 'error', 10);
        });
    }

    render() {
        const {loading, icd, identifier, result} = this.state;
        return (
            <div>
                <h2>PEPPOL Participant Management</h2>
                <div className="form-horizontal smp-home">
                    <div className="flex-box">
                        <div className="flex-item-5">
                            <input type="text" placeholder="ISO 6523 identifier" className="form-control"
                                   value={icd} onChange={e => this.handleFormChange('icd', e.target.value)}/>
                        </div>
                        <div className="flex-item-5">
                            <input type="text" placeholder="Participant identifier" className="form-control"
                                   value={identifier}
                                   onChange={e => this.handleFormChange('identifier', e.target.value)}/>
                        </div>
                        <div className="flex-item-2">
                            <button className="btn btn-primary" onClick={() => this.lookup()}>Search</button>
                        </div>
                    </div>
                    {
                        result.participants.length &&
                        this.renderParticipants()
                    }
                </div>
            </div>
        );
    }

    renderParticipants() {
        const list = [];

        this.state.result.participants.forEach((p) => {
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
                    </div>
                </div>
            </div>);
        });

        return list;
    }
}

export default PeppolSmp;

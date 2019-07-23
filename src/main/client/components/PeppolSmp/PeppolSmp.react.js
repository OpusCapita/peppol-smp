import React from 'react';
import {Components} from '@opuscapita/service-base-ui';
import './PeppolSmp.css';

class PeppolSmp extends Components.ContextComponent {

    state = {};

    constructor(props, context) {
        super(props);
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

    render() {
        // noinspection HtmlUnknownTarget
        return (
            <div>
                <h2>PEPPOL Participant Management</h2>
                <div className="form-horizontal smp-home">
                    <div className="flex-box">
                        <div className="flex-item-5">
                            <input type="text" placeholder="ISO 6523 identifier" className="form-control"/>
                        </div>
                        <div className="flex-item-5">
                            <input type="text" placeholder="Participant identifier" className="form-control"/>
                        </div>
                        <div className="flex-item-2">
                            <button className="btn btn-primary">Search</button>
                        </div>
                    </div>
                    <h3 className="lookup-result-header"><span>9908:21356233</span></h3>
                    <div className="lookup-result row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <div className="col-sm-4">
                                    <label className="control-label btn-link">Access Point</label>
                                </div>
                                <div className="offset-md-1 col-sm-7">
                                    <label className="control-label">OpusCapita Peppol Production Access Point</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-4">
                                    <label className="control-label btn-link">Transport Protocol</label>
                                </div>
                                <div className="offset-md-1 col-sm-7">
                                    <label className="control-label">busdox-transport-as2-ver1p0</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-4">
                                    <label className="control-label btn-link">Access Point URL</label>
                                </div>
                                <div className="offset-md-1 col-sm-7">
                                    <label className="control-label">https://peppol.itella.net/peppol-ap-inbound/as2</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-4">
                                    <label className="control-label btn-link">Access Point Certificate</label>
                                </div>
                                <div className="offset-md-1 col-sm-7">
                                    <label className="control-label">C=FI, O=OpusCapita Solutions Oy, OU=PEPPOL PRODUCTION AP, CN=PNO000104</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-4">
                                    <label className="control-label btn-link">Contact</label>
                                </div>
                                <div className="offset-md-1 col-sm-7">
                                    <label className="control-label">opuscapita.com</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default PeppolSmp;

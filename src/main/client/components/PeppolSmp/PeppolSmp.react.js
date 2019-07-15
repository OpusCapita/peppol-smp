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
                <div className="form-horizontal monitoring-home">
                    <div className="row">
                        <div className="col-lg-6">
                            <a href="/peppol-smp?r=messages" className="thumbnail"
                               onClick={e => this.showPage('messages', e)}>
                                <span className="glyphicon glyphicon-envelope"></span>
                                Lookup
                            </a>
                        </div>
                        <div className="col-lg-6">
                            <a href="/peppol-smp?r=validator" className="thumbnail"
                               onClick={e => this.showPage('validator', e)}>
                                <span className="glyphicon glyphicon-check"></span>
                                Participants
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default PeppolSmp;

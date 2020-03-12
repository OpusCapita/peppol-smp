import React from 'react';
import PropTypes from 'prop-types';
import {Components} from '@opuscapita/service-base-ui';
import ReactTable from 'react-table';
import {ApiBase} from '../../api';
import {Consumer} from "../../api/DocumentTypes";
import 'react-table/react-table.css';
import './ParticipantDetail.css';

class ParticipantDetail extends Components.ContextComponent {

    state = {
        loading: false,
        participant: {}
    };

    static propTypes = {
        icd: PropTypes.string.isRequired,
        identifier: PropTypes.string.isRequired,
    };

    constructor(props, context) {
        super(props);
        this.api = new ApiBase();
    }

    componentDidMount() {
        this.setState({loading: true});

        this.api.getParticipantDetail(this.props.icd, this.props.identifier).then(participant => {
            this.setState({loading: false, participant: participant});

        }).catch(e => {
            this.context.showNotification(e.message, 'error', 10);
            this.setState({loading: false});
        });
    }

    deleteParticipant() {

    }

    editParticipant() {

    }

    getDocumentTypes(documentTypes) {
        console.log(documentTypes);
    }

    render() {
        const {i18n, router} = this.context;
        const {loading, participant} = this.state;

        return (
            <div>
                <h3>Participant Detail</h3>
                <div className="form-horizontal participant-detail">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">PEPPOL ID</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="control-label">{participant.icd}:{participant.identifier}</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Name</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="control-label">{participant.name}</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Contact Info</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="control-label">{participant.contactInfo}</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Register Info</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label
                                        className="control-label">{participant.smpName}-{participant.endpointType}</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="form-submit text-right participant-detail-actions">
                    <button className="btn btn-danger" onClick={e => this.deleteParticipant(e)}>Delete</button>
                    <button className="btn btn-default" onClick={e => this.editParticipant(e)}>Edit</button>
                </div>
                <div>
                    <h3>Document Types</h3>
                    <div className="form-horizontal participant-detail">
                        <div className="row">
                            <div className="col-md-12">
                                <Consumer>
                                    <ReactTable
                                        className="message-detail-history-table"
                                        data={this.getDocumentTypes(documentTypes)}
                                        loading={loading}
                                        columns={[
                                            {
                                                id: 'internalId',
                                                width: 185,
                                                Header: 'ID',
                                                accessor: 'internalId',
                                            },
                                            {
                                                id: 'name',
                                                Header: 'Name',
                                                accessor: 'name',
                                            }
                                        ]}
                                        sorted={[{
                                            id: 'name',
                                            desc: false
                                        }]}
                                        minRows={5}
                                        defaultPageSize={100}
                                    />
                                </Consumer>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="footer-wrapper">
                    <a className='btn btn-default' href="#" onClick={() => router.push('/peppol-smp')}>
                        <span className="icon glyphicon glyphicon-chevron-left"/> Go to List
                    </a>
                </div>
            </div>
        );
    }
}

export default ParticipantDetail;

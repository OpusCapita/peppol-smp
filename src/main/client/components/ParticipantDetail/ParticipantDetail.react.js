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
        participant: {documentTypes:[]}
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

    deleteParticipant(e) {
        e && e.preventDefault();
        const {userData, router, showNotification, showModalDialog, hideModalDialog, showSpinner, hideSpinner} = this.context;

        const onConfirmationClick = (btn) => {
            hideModalDialog();

            if (btn === 'yes') {
                showSpinner();

                setTimeout(() => {
                    this.api.deleteParticipant(this.state.participant.id, userData.id).then(() => {
                        showNotification('The participant is deleted successfully', 'info', 3);
                    }).catch(e => {
                        showNotification(e.message, 'error', 10);
                    }).finally(() => {
                        hideSpinner();
                        router.push('/peppol-smp');
                    });
                }, 500);
            }
        };

        const modalTitle = "Delete";
        const modalText = `Are you sure?`;
        const modalButtons = {no: 'No', yes: 'Yes'};
        showModalDialog(modalTitle, modalText, onConfirmationClick, modalButtons);
    }



    editParticipant(e) {
        e && e.preventDefault();
        this.context.router.push(`/peppol-smp/edit/${this.state.participant.id}`);
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

    getRegisteredAt(participant) {
        return participant.smpName === "TICKSTAR" ? this.context.i18n.formatDateTime(participant.registeredAt) : participant.registeredAt;
    }

    render() {
        const {i18n, router} = this.context;
        const {loading, participant} = this.state;

        return (
            <div>
                <h3>{participant.icd}:{participant.identifier}</h3>
                <div className="form-horizontal participant-detail">
                    <div className="row">
                        <div className="col-md-12">
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
                                    <label className="control-label btn-link">Contact Name</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="control-label">{participant.contactName}</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Contact Email</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="control-label">{participant.contactEmail}</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Contact Phone</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label className="control-label">{participant.contactPhone}</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-3">
                                    <label className="control-label btn-link">Register Info</label>
                                </div>
                                <div className="offset-md-1 col-md-8">
                                    <label
                                        className="control-label">{participant.smpName}-{participant.endpointType} at {this.getRegisteredAt(participant)}</label>
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
                    <h4>Document Types</h4>
                    <div className="form-horizontal participant-detail">
                        <div className="row">
                            <div className="col-md-12">
                                <Consumer>
                                    {({ documentTypes }) => (
                                        <ReactTable
                                            className="message-detail-history-table"
                                            data={this.getDocumentTypes(documentTypes)}
                                            loading={loading}
                                            columns={[
                                                {
                                                    id: 'id',
                                                    width: 185,
                                                    Header: 'ID',
                                                    accessor: 'id',
                                                },
                                                {
                                                    id: 'name',
                                                    Header: 'Name',
                                                    accessor: 'name',
                                                }
                                            ]}
                                            sorted={[{
                                                id: 'id',
                                                desc: false
                                            }]}
                                            minRows={5}
                                            defaultPageSize={100}
                                        />
                                    )}
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

import React from 'react';
import {Components} from '@opuscapita/service-base-ui';
import ReactTable from 'react-table';
import ReactTooltip from 'react-tooltip';
import {ApiBase, Countries} from '../../api';
import Select from '@opuscapita/react-select';
import 'react-table/react-table.css';
import './ParticipantList.css';

class ParticipantList extends Components.ContextComponent {

    static types = [
        'TEST',
        'PROD'
    ];

    static smpNames = [
        'DIFI',
        'TICKSTAR'
    ];

    state = {
        init: true,
        loading: false,
        participantList: [],
        searchValues: {},
        totalCount: -1,
        pagination: {},
    };

    constructor(props, context) {
        super(props);

        this.api = new ApiBase();
    }

    async loadParticipantList(tableState) {
        this.setState({loading: true});
        try {
            const response = await this.api.getParticipantList();
            this.setState({participantList: response});
        } catch (e) {
            this.context.showNotification(e.message, 'error', 10);
        } finally {
            this.setState({loading: false});
        }
    }

    getParticipantList(prm) {
        console.log(prm);
        return this.state.participantList.filter(this.filterParticipants);
    }

    filterParticipants(participant) {
        const {searchValues} = this.state;

        if (searchValues.icd && searchValues.icd !== '') {
            if (participant.icd !== searchValues.icd) {
                return false;
            }
        }

        if (searchValues.identifier && searchValues.identifier !== '') {
            if (participant.identifier !== searchValues.identifier) {
                return false;
            }
        }

        if (searchValues.name && searchValues.name !== '') {
            if (!participant.name.toLowerCase().includes(searchValues.name.toLowerCase())) {
                return false;
            }
        }

        if (searchValues.countries && searchValues.countries.length) {
            if (!searchValues.countries.includes(participant.country)) {
                return false;
            }
        }

        if (searchValues.smpNames && searchValues.smpNames.length) {
            if (!searchValues.smpNames.includes(participant.smpName)) {
                return false;
            }
        }

        if (searchValues.endpointTypes && searchValues.endpointTypes.length) {
            if (!searchValues.endpointTypes.includes(participant.endpointType)) {
                return false;
            }
        }

        return true;
    }


    mapSmpsSelect() {
        return ParticipantList.smpNames.map(value => {
            return {value: value, label: value};
        });
    }

    mapTypesSelect() {
        return ParticipantList.types.map(value => {
            return {value: value, label: value};
        });
    }

    mapCountriesSelect() {
        return Countries.map(value => {
            return {value: value.code, label: value.name};
        });
    }

    handleSearchFormChange(field, value) {
        const {searchValues} = this.state;

        if (Array.isArray(value))
            searchValues[field] = value.map(val => val.value);
        else
            searchValues[field] = value;

        this.setState({searchValues});
    }

    resetSearch() {
        const searchValues = {
            icd: '',
            name: '',
            identifier: '',
            smpNames: [],
            countries: [],
            endpointTypes: []
        };

        this.setState({searchValues}, () => this.loadParticipantList());
    }

    showAddParticipantPage() {
        this.context.router.push('/peppol-smp/create');
    }

    render() {
        const {i18n} = this.context;
        const {loading, participantList, pagination, totalCount, searchValues} = this.state;

        return (
            <div>
                <h3>Participant List
                    <button className="btn btn-info participant-add-btn" onClick={() => this.showAddParticipantPage()}>
                        New Participant
                    </button>
                </h3>
                <div>
                    <div className="form-horizontal participant-search">
                        <div className="row">
                            <div className="col-md-6">
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">ICD Number</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <input type="text" className="form-control" value={searchValues.icd}
                                               onChange={e => this.handleSearchFormChange('icd', e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">Name</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <input type="text" className="form-control" value={searchValues.name}
                                               onChange={e => this.handleSearchFormChange('name', e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">SMP</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <Select className="react-select" isMulti={true}
                                                options={this.mapSmpsSelect()}
                                                onChange={value => this.handleSearchFormChange('smpNames', value)}
                                                value={searchValues.smpNames && searchValues.smpNames.map(cts => ({
                                                    label: cts,
                                                    value: cts
                                                }))}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">Participant ID</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <input type="text" className="form-control" value={searchValues.identifier}
                                               onChange={e => this.handleSearchFormChange('identifier', e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">Country</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <Select className="react-select" isMulti={true}
                                                options={this.mapCountriesSelect()}
                                                onChange={value => this.handleSearchFormChange('countries', value)}
                                                value={searchValues.countries && searchValues.countries.map(cts => ({
                                                    label: cts,
                                                    value: cts
                                                }))}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">Type</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <Select className="react-select" isMulti={true}
                                                options={this.mapTypesSelect()}
                                                onChange={value => this.handleSearchFormChange('endpointTypes', value)}
                                                value={searchValues.endpointTypes && searchValues.endpointTypes.map(cts => ({
                                                    label: cts,
                                                    value: cts
                                                }))}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="form-submit text-right">
                        <button className="btn btn-link" onClick={() => this.resetSearch()}>Reset</button>
                        <button className="btn btn-primary" onClick={() => this.loadParticipantList()}>Filter</button>
                    </div>
                    <hr/>
                </div>

                <ReactTable
                    className="participant-list-table"
                    loading={loading}
                    data={this.getParticipantList(prm)}
                    onFetchData={() => this.loadParticipantList()}
                    minRows={10}
                    defaultPageSize={10}
                    defaultSorted={[{id: 'registeredAt', desc: true}]}

                    columns={[
                        {
                            id: 'identifier',
                            accessor: row => row,
                            Header: 'ID',
                            Cell: ({value}) => <span>{`${value.icd}:${value.identifier}`}</span>
                        },
                        {
                            id: 'name',
                            accessor: 'name',
                            Header: 'Name'
                        },
                        {
                            width: 100,
                            accessor: 'smpName',
                            Header: 'SMP'
                        },
                        {
                            id: 'endpointType',
                            width: 75,
                            accessor: 'endpointType',
                            Header: 'Type',
                            Cell: ({value}) =>
                                <span
                                    className={`label label-${value === 'PROD' ? 'success' : 'info'}`}>{value.toLowerCase()}</span>
                        },
                        {
                            id: 'registeredAt',
                            width: 150,
                            accessor: 'registeredAt',
                            Header: 'Registered At',
                            Cell: props => <span>{i18n.formatDateTime(props.value)}</span>
                        }
                    ]}
                />
                <div className="text-center media">
                    <p>{`${pagination.page * pagination.pageSize} to ${Math.min((pagination.page * pagination.pageSize + pagination.pageSize), totalCount)} of ${totalCount} participants`}</p>
                </div>
            </div>
        );
    }
}

export default ParticipantList;

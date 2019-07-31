import React from 'react';
import PropTypes from 'prop-types';
import {Components} from '@opuscapita/service-base-ui';
import ReactTable from 'react-table';
import ReactTooltip from 'react-tooltip';
import {ApiBase} from '../../api';
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

    static countries = [
        {code: 'SK'},
        {code: 'SE'},
        {code: 'PT'},
        {code: 'PL'},
        {code: 'NO'},
        {code: 'NL'},
        {code: 'MT'},
        {code: 'LV'},
        {code: 'IT'},
        {code: 'IE'},
        {code: 'GI'},
        {code: 'GB'},
        {code: 'FR'},
        {code: 'FI'},
        {code: 'ES'},
        {code: 'EE'},
        {code: 'DK'},
        {code: 'DE'},
        {code: 'CH'},
        {code: 'BE'},
        {code: '00'}
    ];

    state = {
        loading: false,
        participantList: [],
        searchValues: {},
        showSearch: true,
        totalCount: -1,
        pagination: {},
    };

    constructor(props, context) {
        super(props);

        this.api = new ApiBase();
    }

    async loadParticipantList(tableState) {
        this.setState({loading: true});
        let {pagination, searchValues} = this.state;

        try {
            if (tableState) {
                pagination.page = tableState.page;
                pagination.pageSize = tableState.pageSize;
                pagination.sorted = tableState.sorted;
            } else {
                pagination.page = 0;
            }

            const response = await this.api.getParticipantList(pagination, searchValues);
            this.setState({participantList: response.data, totalCount: response.totalCount});
        }
        catch (e) {
            this.context.showNotification(e.message, 'error', 10);
        }
        finally {
            this.setState({loading: false});
        }
    }

    showParticipantLookup(participant) {
        const parts = participant.split(":");
        window.open(`https://my.galaxygw.com/participantlookup#/${parts[0]}/${parts[1]}`, '_blank');
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
        return ParticipantList.countries.map(value => {
            return {value: value.code, label: value.code};
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

    render() {
        const {i18n} = this.context;
        const {loading, participantList, pagination, totalCount, searchValues, showSearch} = this.state;

        return (
            <div>
                <h3>Participant List</h3>
                {
                    showSearch &&
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
                }

                <ReactTable
                    className="participant-list-table"
                    loading={loading}
                    data={participantList}
                    onFetchData={(state) => this.loadParticipantList(state)}

                    manual
                    minRows={10}
                    pages={Math.ceil(totalCount / (pagination.pageSize || 10))}
                    defaultPageSize={10}
                    pageSizeOptions={[5, 10, 20, 50, 100, 1000]}
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
                                <span className={`label label-${value === 'PROD' ? 'success' : 'info'}`}>{value.toLowerCase()}</span>
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

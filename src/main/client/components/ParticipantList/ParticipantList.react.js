import React from 'react';
import {Components} from '@opuscapita/service-base-ui';
import ReactTable from 'react-table';
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

    state = {
        loading: false,
        participantList: [],
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

    showAddParticipantPage() {
        this.context.router.push('/peppol-smp/create');
    }

    render() {
        const {i18n} = this.context;
        const {loading, participantList} = this.state;

        return (
            <div>
                <h3>Participant List
                    <button className="btn btn-info participant-add-btn" onClick={() => this.showAddParticipantPage()}>
                        New Participant
                    </button>
                </h3>
                <!--
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
                -->
                <ReactTable
                    className="participant-list-table"
                    loading={loading}
                    data={participantList}
                    filterable={true}
                    onFetchData={() => this.loadParticipantList()}
                    minRows={10}
                    defaultPageSize={10}
                    defaultSorted={[{id: 'registeredAt', desc: true}]}
                    defaultFilterMethod={(filter, row) => String(row[filter.id]) === filter.value}

                    columns={[
                        {
                            id: 'identifier',
                            accessor: row => row,
                            Header: 'ID',
                            Cell: ({value}) => <span>{`${value.icd}:${value.identifier}`}</span>,
                            filterMethod: (filter, row) => {
                                const rowVal = row[filter.id];
                                if (filter.value.includes(":")) {
                                    return rowVal === filter.value;
                                }
                                for (let part of rowVal.split(":")) {
                                    if (part === filter.value) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                        },
                        {
                            id: 'name',
                            accessor: 'name',
                            Header: 'Name',
                            filterMethod: (filter, row) => row[filter.id].includes(filter.value)
                        },
                        {
                            id: 'endpointType',
                            width: 75,
                            accessor: 'endpointType',
                            Header: 'Type',
                            Cell: ({value}) =>
                                <span className={`label label-${value === 'PROD' ? 'success' : 'info'}`}>{value.toLowerCase()}</span>,
                            filterMethod: (filter, row) => {
                                if (filter.value === "ALL") {
                                    return true;
                                }
                                return row[filter.id] === filter.value;
                            },
                            Filter: ({ filter, onChange }) =>
                                <select
                                    onChange={event => onChange(event.target.value)}
                                    style={{ width: "100%" }}
                                    value={filter ? filter.value : "PROD"}
                                >
                                    <option value="ALL">ALL</option>
                                    <option value="TEST">TEST</option>
                                    <option value="PROD">PROD</option>
                                </select>
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
                    <p>{`Total ${participantList.length} participants`}</p>
                </div>
            </div>
        );
    }
}

export default ParticipantList;

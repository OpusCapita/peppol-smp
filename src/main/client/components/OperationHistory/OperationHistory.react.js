import React from 'react';
import {Components} from '@opuscapita/service-base-ui';
import ReactTable from 'react-table';
import {ApiBase, Countries} from '../../api';
import Select from '@opuscapita/react-select';
import 'react-table/react-table.css';
import './OperationHistory.css';

class OperationHistory extends Components.ContextComponent {

    static types = [
        'REGISTER',
        'BULK_REGISTER',
        'MODIFY_INFO',
        'ADD_DOCUMENT',
        'REMOVE_DOCUMENT',
        'DELETE'
    ];

    state = {
        loading: false,
        operationHistory: [],
        searchValues: {},
        totalCount: -1,
        pagination: {},
    };

    constructor(props, context) {
        super(props);
        this.api = new ApiBase();
    }

    async loadHistory(tableState) {
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

            const response = await this.api.getOperationHistory(pagination, searchValues);
            this.setState({operationHistory: response.data, totalCount: response.totalCount});

        } catch (e) {
            this.context.showNotification(e.message, 'error', 10);

        } finally {
            this.setState({loading: false});
        }
    }

    mapTypesSelect() {
        return OperationHistory.types.map(value => {
            return {value: value, label: value};
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
            user: '',
            participant: '',
            startDate: '',
            endDate: '',
            types: []
        };

        this.setState({searchValues}, () => this.loadHistory());
    }

    showParticipantDetail(e, history) {
        e && e.preventDefault();
        this.context.router.push(`/peppol-smp/detail/${history.participant.split(":")[0]}/${history.participant.split(":")[1]}`);
    }

    render() {
        const {i18n} = this.context;
        const {loading, operationHistory, pagination, totalCount, searchValues} = this.state;

        return (
            <div>
                <h3>Operation History</h3>

                <div>
                    <div className="form-horizontal participant-search">
                        <div className="row">
                            <div className="col-md-6">
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">User</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <input type="text" className="form-control" value={searchValues.user}
                                               onChange={e => this.handleSearchFormChange('user', e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">Operation Type</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <Select className="react-select" isMulti={true}
                                                options={this.mapTypesSelect()}
                                                onChange={value => this.handleSearchFormChange('types', value)}
                                                value={searchValues.types && searchValues.types.map(cts => ({
                                                    label: cts,
                                                    value: cts
                                                }))}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">Start Date</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <Components.DatePicker
                                            showIcon={false}
                                            dateFormat={i18n.dateTimeFormat}
                                            onChange={e => this.handleSearchFormChange('startDate', e.date)}
                                            value={searchValues.startDate && new Date(searchValues.startDate)}
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
                                        <input type="text" className="form-control" value={searchValues.participant}
                                               onChange={e => this.handleSearchFormChange('participant', e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">&nbsp;</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        &nbsp; {/*  reserved  */}
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-3">
                                        <label className="control-label">End Date</label>
                                    </div>
                                    <div className="offset-md-1 col-md-8">
                                        <Components.DatePicker
                                            showIcon={false}
                                            dateFormat={i18n.dateTimeFormat}
                                            onChange={e => this.handleSearchFormChange('endDate', e.date)}
                                            value={searchValues.endDate && new Date(searchValues.endDate)}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="form-submit text-right">
                        <button className="btn btn-link" onClick={() => this.resetSearch()}>Reset</button>
                        <button className="btn btn-primary" onClick={() => this.loadHistory()}>Filter</button>
                    </div>
                    <hr/>
                </div>

                <ReactTable
                    className="participant-list-table"
                    loading={loading}
                    data={operationHistory}
                    onFetchData={(state) => this.loadHistory(state)}

                    manual
                    minRows={10}
                    pages={Math.ceil(totalCount / (pagination.pageSize || 10))}
                    defaultPageSize={10}
                    pageSizeOptions={[10, 20, 50, 100, 1000]}
                    defaultSorted={[{id: 'date', desc: true}]}

                    columns={[
                        {
                            id: 'participant',
                            accessor: row => row,
                            Header: 'Participant ID',
                            width: 200,
                            Cell: ({value}) =>
                                <span>
                                    <a href={`/peppol-smp?r=detail/${value.participant.split(":")[0]}/${value.participant.split(":")[1]}`}
                                       onClick={(e) => this.showParticipantDetail(e, value)}
                                       className="btn btn-link detail-link">
                                        <span>{value.participant}</span>
                                    </a>
                                </span>

                        },
                        {
                            width: 200,
                            accessor: 'type',
                            Header: 'Operation Type'
                        },
                        {
                            id: 'user',
                            accessor: 'user',
                            Header: 'User'
                        },
                        {
                            id: 'date',
                            width: 150,
                            accessor: 'date',
                            Header: 'Operation Time',
                            Cell: props => <span>{i18n.formatDateTime(props.value)}</span>
                        }
                    ]}
                />
                <div className="text-center media">
                    <p>{`${pagination.page * pagination.pageSize} to ${Math.min((pagination.page * pagination.pageSize + pagination.pageSize), totalCount)} of ${totalCount} operation history`}</p>
                </div>

                <div className="footer-wrapper">
                    <a className='btn btn-default' href="#" onClick={() => router.push('/peppol-smp')}>
                        <span className="icon glyphicon glyphicon-chevron-left"/> Go Back
                    </a>
                </div>
            </div>
        );
    }
}

export default OperationHistory;

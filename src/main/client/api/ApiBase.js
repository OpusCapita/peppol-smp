import request from 'superagent';
import ApiError from './ApiError';

class ApiBase {

    ajax = request;

    getTransmissionList(pagination, filter) {
        return this.ajax.post(`/peppol-monitor/api/get-transmissions`).send({pagination, filter}).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }
}

export default ApiBase;

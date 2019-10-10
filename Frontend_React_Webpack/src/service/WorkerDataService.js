import axios from 'axios'

const INSTRUCTOR_API_URL = 'http://localhost:8080';

class workerDataService {

    retrieveWorkerTree(){
        return axios.get(`${INSTRUCTOR_API_URL }/workers/tree`);
    }

    retrieveAllWorkers() {
        return axios.get(`${INSTRUCTOR_API_URL }/workers`);
    }
    retrieveAllPaginationWorkers(name,orgName,page,sizePage) {
        return axios.get(`${INSTRUCTOR_API_URL }/workers/search?name=${name}&org=${orgName}&page=${page}&size=${sizePage}`);
    }
    
    retrieveWorker(id) {
        return axios.get(`${INSTRUCTOR_API_URL }/workers/id/${id}`);
    }

    createWorker(worker) {
        return axios.post(`${INSTRUCTOR_API_URL }/workers/`, worker);
    }

    updateWorker(id, worker) {
        return axios.put(`${INSTRUCTOR_API_URL }/workers/id/${id}`, worker);
    }

    deleteWorker(id) {
        return axios.delete(`${INSTRUCTOR_API_URL }/workers/id/${id}`);
    }
}

export default new workerDataService()
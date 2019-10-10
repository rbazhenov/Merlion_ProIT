import axios from 'axios'

const INSTRUCTOR_API_URL = 'http://localhost:8080';

class OrganizationDataService {

    retrieveOrganizationTree(){
        return axios.get(`${INSTRUCTOR_API_URL }/organizations/tree`);
    }

    retrieveAllOrganizations() {
        return axios.get(`${INSTRUCTOR_API_URL }/organizations`);
    }

    retrieveAllPaginationOrganizations(name,page,size) {
        return axios.get(`${INSTRUCTOR_API_URL }/organizations/search?name=${name}&page=${page}&size=${size}`);
    }
    
    retrieveOrganization(id) {
        return axios.get(`${INSTRUCTOR_API_URL }/organizations/id/${id}`);
    }

    createOrganization(organization) {
        return axios.post(`${INSTRUCTOR_API_URL }/organizations/`, organization);
    }

    updateOrganization(id, organization) {
        return axios.put(`${INSTRUCTOR_API_URL }/organizations/id/${id}`, organization);
    }

    deleteOrganization(id) {
        return axios.delete(`${INSTRUCTOR_API_URL }/organizations/id/${id}`);
    }
}

export default new OrganizationDataService()
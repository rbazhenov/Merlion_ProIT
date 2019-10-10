import React, { Component } from 'react'
import OrganizationDataService from '../service/OrganizationDataService.js';
import axios from 'axios';
import Pagination from "react-js-pagination";
import queryString from 'query-string';

class ListOrganizations extends Component {
    constructor(props) {
        super(props);
        this.state = {
            organizations: [],
            message: null,
            errorMessage:null,
            searchName:"",
            errors:[],

            organizationsDetails: [],
            activePage:1,
            sizePage:3,
            totalPages: null,
            itemsCountPerPage:null,
            totalItemsCount:null
        };
        this.refreshOrganizations = this.refreshOrganizations.bind(this);
        this.deleteOrganizationClicked = this.deleteOrganizationClicked.bind(this);
        this.updateOrganizationClicked = this.updateOrganizationClicked.bind(this);
        this.addOrganizationClicked = this.addOrganizationClicked.bind(this);
        this.handleSearch = this.handleSearch.bind(this);

        this.onClickSearch = this.onClickSearch.bind(this);
        this.onClickCancelSearch = this.onClickCancelSearch.bind(this);
        
        this.handlePageChange = this.handlePageChange.bind(this);
        this.fetchURL = this.fetchURL.bind(this);
        this.handleSizePage = this.handleSizePage.bind(this);
    }
    fetchURL(name,page,size) {
        page=page-1;
        axios.get(`http://localhost:8080/organizations/search?name=${name}&page=${page}&size=${size}`)
            .then( response => {
                const totalPages = response.data.totalPages;
                const itemsCountPerPage = response.data.size;
                const totalItemsCount = response.data.totalElements;

                this.setState({totalPages: totalPages});
                this.setState({totalItemsCount: totalItemsCount});
                this.setState({itemsCountPerPage: itemsCountPerPage});
                this.setState({
                    activePage: page+1,
                    searchName: name,
                    sizePage: size
                });
                const results = response.data.content;

                this.setState({organizationsDetails: results});
                console.log("All data ");
                console.log(response);
                }
            );
        
    }

    handleSearch(evt){
        this.setState({searchName : evt.target.value})
    }
    onClickSearch(){
        this.fetchURL(this.state.searchName,0,this.state.sizePage);
        this.refreshOrganizations("find")
    }

    onClickCancelSearch(){
        this.setState({searchName:"", activePage: 1});
        this.fetchURL("",1,this.state.sizePage);
        this.refreshOrganizations("cancel");
        this.props.history.push(`/organizations/search?name=&page=1&size=${this.state.sizePage}`);
    }

    handleSizePage(evt){
        this.setState({sizePage:evt.target.value});
    }

    handlePageChange(pageNumber) {
        this.props.history.push(`/organizations/search?name=${this.state.searchName}&page=${pageNumber}&size=${this.state.sizePage}`);
        this.setState({activePage: pageNumber});
        this.fetchURL(this.state.searchName,pageNumber,this.state.sizePage);
        
    }

    componentDidMount() {
        const values = queryString.parse(this.props.location.search);
        const name = (values.name !== undefined) ? values.name : "";
        const page = (values.page !==undefined)? values.page : 1;
        const size = (values.size !==undefined)? values.size : this.state.sizePage;
        this.fetchURL(name,page,size);
    }

    refreshOrganizations(flagToRefresh) {
        switch(flagToRefresh){
            case "delete":
                OrganizationDataService.retrieveAllPaginationOrganizations(this.state.searchName, 0,this.state.sizePage)
                .then(
                    response => {
                    this.setState({ organizationsDetails: response.data.content})
                    }
                );
            break;
            case "find":
                OrganizationDataService.retrieveAllPaginationOrganizations(this.state.searchName, 0,this.state.sizePage);
                this.props.history.push(`/organizations/search?name=${this.state.searchName}&page=1&size=${this.state.sizePage}`);
            break;
            case "cancel":
                OrganizationDataService.retrieveAllPaginationOrganizations("", 0,this.state.sizePage);
            break;
        }
    }

    deleteOrganizationClicked(id) {
        axios.delete(`http://localhost:8080/organizations/id/${id}`)
            .then(response => {
                console.log(response.status);
                this.setState({ errorMessage: null, message: `Delete of organization ${id} successful` });
                this.fetchURL(this.state.searchName,0,this.state.sizePage);
                this.refreshOrganizations("delete");
            })
            .catch(
                error => {this.errors = [];
                console.log(error.response);
                this.setState({ message:null, errorMessage: `Organization id(${id}) cannot be deleted` });
                }
            )
    }

    addOrganizationClicked() {
        this.props.history.push(`/organizations/id/-1`)
    }

    updateOrganizationClicked(id) {
        console.log('update ' + id);
        this.props.history.push(`/organizations/id/${id}`)
    }

    render() {
        return (
            <div className="container">
                <h3>Organizations</h3>
                {this.state.message && <div className="alert alert-success">{this.state.message}</div>}
                {this.state.errorMessage && <div className="alert alert-warning">{this.state.errorMessage}</div>}
                <div className="containerSearchDiv">
                    <div className="paginationTopDiv">
                        <div className="searchOrgDiv">
                            <table>
                                <tbody><tr>
                                    <th><label> Name: </label></th>
                                    <th><input size="15" type="text" value={this.state.searchName} onChange={evt => this.handleSearch(evt)}/></th>
                                    <th><label> Records on page: </label></th>
                                    <th><input size ="1" type="text"  onChange={evt => this.handleSizePage(evt)}/></th>
                                    <th><button className="btn btn-info" onClick={() =>this.onClickSearch()}>Search/resize</button></th>
                                    <th><button className="btn btn-danger" onClick={() =>this.onClickCancelSearch()}>Cancel</button></th>
                                </tr></tbody>
                            </table>
                        </div>
                    </div>
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Main organization</th>
                                <th>Workers</th>
                                <th>Update</th>
                                <th>Delete</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.organizationsDetails.map(
                                    org =>
                                        <tr key={org.id}>
                                             <td>{org.id}</td>
                                             <td>{org.name}</td>
                                             <td>{org.mainOrganizationName}</td>
                                             <td>{org.workersCount}</td>
                                             <td><button className="btn btn-success" onClick={() => this.updateOrganizationClicked(org.id)}>Update</button></td>
                                             <td><button className="btn btn-warning" onClick={() => this.deleteOrganizationClicked(org.id, org.mainOrganizationId)}>Delete</button></td>
                                        </tr>
                                )
                            }
                            <tr><td>
                            <div className="row">
                                <button className="btn btn-success" onClick={this.addOrganizationClicked}>Add</button>
                            </div>
                            </td></tr>
                        </tbody>
                    </table>
                </div>
                <div className="d-flex justify-content-center">
                    <Pagination
                    // hideNavigation
                    // hideDisabled
                    // prevPageText={"prev"}
                    // nextPageText={"next"}
                    activePage={this.state.activePage}
                    itemsCountPerPage={this.state.itemsCountPerPage}
                    totalItemsCount={this.state.totalItemsCount}
                    pageRangeDisplayed={3}
                    itemClass='page-item'
                    linkClass='btn btn-light'
                    onChange={this.handlePageChange}
                    />
                </div>
            </div>
        )
    }
}

export default ListOrganizations
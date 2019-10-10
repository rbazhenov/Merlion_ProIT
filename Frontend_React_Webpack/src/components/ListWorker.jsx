import React, { Component } from 'react'
import WorkerDataService from '../service/WorkerDataService.js';
import axios from 'axios';
import Pagination from "react-js-pagination";
import queryString from 'query-string';

class ListWorkersComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            workers: [],
            message: null,
            errorMessage: null,
            searchName:"",
            searchOrgName:"",
            errors:[],

            workersDetails: [],
            activePage:1,
            sizePage:3,
            totalPages: null,
            itemsCountPerPage:null,
            totalItemsCount:null
        };
        this.refreshWorkers = this.refreshWorkers.bind(this);
        this.deleteWorkerClicked = this.deleteWorkerClicked.bind(this);
        this.updateWorkerClicked = this.updateWorkerClicked.bind(this);
        this.addWorkerClicked = this.addWorkerClicked.bind(this);

        this.handleSearchName = this.handleSearchName.bind(this);
        this.handleSearchOrgName = this.handleSearchOrgName.bind(this);

        this.onClickSearch = this.onClickSearch.bind(this);
        this.onClickCancelSearch = this.onClickCancelSearch.bind(this);

        this.handlePageChange = this.handlePageChange.bind(this);
        this.fetchURL = this.fetchURL.bind(this);
        this.handleSizePage = this.handleSizePage.bind(this);
    }

    fetchURL(name,orgName,page,size) {
        page=page-1;
        axios.get(`http://localhost:8080/workers/search?name=${name}&org=${orgName}&page=${page}&size=${size}`)
            .then( response => {

                const totalPages = response.data.totalPages;
                const itemsCountPerPage = response.data.size;
                const totalItemsCount = response.data.totalElements;

                this.setState({totalPages: totalPages});
                this.setState({totalItemsCount: totalItemsCount});
                this.setState({itemsCountPerPage: itemsCountPerPage});
                this.setState({
                    activePage: page,
                    searchName: name,
                    sizePage: size,
                    searchOrgName:orgName
                });
                const results = response.data.content;

                this.setState({workersDetails: results});
                console.log("All data ");
                console.log(response)
                }
            );
    }

    handleSearchName(evt){
        this.setState({searchName : evt.target.value})
    }
    handleSearchOrgName(evt){
        this.setState({searchOrgName: evt.target.value})
    }

    onClickSearch(){
        this.fetchURL(this.state.searchName,this.state.searchOrgName,0,this.state.sizePage);
        this.refreshWorkers("find");
    }
    onClickCancelSearch(){
        this.setState({searchName:"", searchOrgName:"", activePage: 1});
        this.fetchURL("","",1,this.state.sizePage);
        this.refreshWorkers("cancel");
        this.props.history.push(`/workers/search?name=&org=&page=1&size=${this.state.sizePage}`);
    }

    handleSizePage(evt){
        this.setState({sizePage:evt.target.value});
    }

    handlePageChange(pageNumber) {
        this.props.history.push(`/workers/search?name=${this.state.searchName}&org=${this.state.searchOrgName}&page=${pageNumber}&size=${this.state.sizePage}`);
        this.setState({activePage: pageNumber});
        this.fetchURL(this.state.searchName,this.state.searchOrgName,pageNumber,this.state.sizePage);
    }

    componentDidMount() {
        const values = queryString.parse(this.props.location.search);
        const name = (values.name !==undefined)? values.name : "";
        const org = (values.org !==undefined)? values.org : "";
        const page = (values.page !==undefined)? values.page : 1;
        const size = (values.size !==undefined)? values.size : this.state.sizePage;
        this.fetchURL(name,org,page,size);
    }

    refreshWorkers(flagToRefresh) {
        switch(flagToRefresh){
            case "delete":
                WorkerDataService.retrieveAllPaginationWorkers(this.state.searchName,this.state.searchOrgName, 0,this.state.sizePage)
                    .then(
                        response => {
                        this.setState({workersDetails: response.data.content})
                        }
                    );
            break;
            case "find":
                WorkerDataService.retrieveAllPaginationWorkers(this.state.searchName,this.state.searchOrgName, 0,this.state.sizePage);
                this.props.history.push(`/workers/search?name=${this.state.searchName}&org=${this.state.searchOrgName}&page=1&size=${this.state.sizePage}`);
            break;
            case "cancel":
                WorkerDataService.retrieveAllPaginationWorkers("","", 0,this.state.sizePage);
            break;
        }
    }

    deleteWorkerClicked(id) {
        axios.delete(`http://localhost:8080/workers/id/${id}`)
            .then(response => {
                console.log(response.status);
                this.setState({ errorMessage: null, message: `Delete of worker ${id} successful` });
                this.fetchURL(this.state.searchName,this.state.searchOrgName,0,this.state.sizePage);
                this.refreshWorkers("delete");
            })
            .catch(
                error => {this.errors = [];
                console.log(error.response);
                this.setState({ message:null, errorMessage: `Worker id(${id}) cannot be deleted` });
                }
            )
    }

    addWorkerClicked() {
        this.props.history.push(`/workers/id/-1`)
    }

    updateWorkerClicked(id) {
        console.log('update ' + id);
        this.props.history.push(`/workers/id/${id}`)
    }

    render() {
        return (
            <div className="container">
                <h3>Workers</h3>
                {this.state.message && <div className="alert alert-success">{this.state.message}</div>}
                {this.state.errorMessage && <div className="alert alert-warning">{this.state.errorMessage}</div>}
                <div className="containerSearchDiv">
                    <div className="paginationTopDiv">
                        <div className="searchWorkerDiv">
                            <table>
                                <tbody><tr>
                                    <th><label> Name: </label></th>
                                    <th><input type="text" size="15" value={this.state.searchName} onChange={evt => this.handleSearchName(evt)}/></th>
                                    <th><label> Organization name: </label></th>
                                    <th><input type="text" size="15" value={this.state.searchOrgName} onChange={evt => this.handleSearchOrgName(evt)}/></th>
                                    <th><label>Records on page: </label></th>
                                    <th><input size ="1" type="text"  onChange={evt => this.handleSizePage(evt)}/></th>
                                    <th><button className="btn btn-info" onClick={() => this.onClickSearch()}>Search/resize</button></th>
                                    <th><button className="btn btn-danger" onClick={() => this.onClickCancelSearch()}>Cancel</button></th>
                                </tr></tbody>
                            </table>
                        </div>
                    </div>
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Main Worker</th>
                                <th>Main Organization</th>
                                <th>Update</th>
                                <th>Delete</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.workersDetails.map(
                                    worker =>
                                        <tr key={worker.id}>
                                            <td>{worker.id}</td>
                                            <td>{worker.name}</td>
                                            <td>{worker.mainWorkerName}</td>
                                            <td>{worker.organizationName}</td>
                                            <td><button className="btn btn-success" onClick={() => this.updateWorkerClicked(worker.id)}>Update</button></td>
                                            <td><button className="btn btn-warning" onClick={() => this.deleteWorkerClicked(worker.id)}>Delete</button></td>
                                        </tr>
                                )
                            }
                            <tr><td>
                            <div className="row">
                                <button className="btn btn-success" onClick={this.addWorkerClicked}>Add</button>
                            </div>
                            </td></tr>
                        </tbody>
                    </table>
                </div>
                <div className="d-flex justify-content-center">
                    <Pagination
                    hideNavigation
                    activePage={this.state.activePage}
                    itemsCountPerPage={this.state.itemsCountPerPage}
                    totalItemsCount={this.state.totalItemsCount}
                    pageRangeDisplayed={4}
                    itemClass='page-item'
                    linkClass='btn btn-light'
                    onChange={this.handlePageChange}
                    />
                </div>
            </div>
        )
    }
}

export default ListWorkersComponent
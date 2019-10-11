import React, { Component } from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik';
import WorkerDataService from '../service/WorkerDataService.js';
import OrganizationDataService from '../service/OrganizationDataService.js';
import axios from 'axios';
class SoloWorker extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            name: '',
            workers: [],
            selectedMainWorker:0,
            mainWorkerId: 0,
            defaultMainWorker:0,
            message: null,

            organizations:[],
            organizationId: 0,
            selectedOrganization: 0,
            defaultOrganization: 0
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.selectWorker = this.selectWorker.bind(this);
        this.selectOrganization = this.selectOrganization.bind(this);
        this.handleBack = this.handleBack.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }

    componentDidMount() {
        if(this.state.id){
            this.uploadWorkers();
            this.uploadOrganizations();
            if(this.state.id != -1){
                this.uploadThisWorker();
            }
        }
    }

    uploadWorkers() {
        WorkerDataService.retrieveAllWorkers()
        .then(response => this.setState({
            workers: response.data.content
        }))
    }
    uploadOrganizations() {
        OrganizationDataService.retrieveAllOrganizations()
        .then(response => this.setState({
            organizations: response.data.content
        }))
    }

    uploadThisWorker(){
        WorkerDataService.retrieveWorker(this.state.id)
            .then(response => this.setState({
                name: response.data.name,
                mainWorkerId: response.data.mainWorkerName,
                selectedMainWorker: response.data.mainWorkerId,
                defaultMainWorker:response.data.mainWorkerId,
                organizationId: response.data.organizationName,
                selectedOrganization: response.data.organizationId,
                defaultOrganization: response.data.organizationId
            }))
    }

    selectOrganization (event){
        if(event.target.value =="" && this.state.defaultOrganization!=""){
            this.setState({
                selectedOrganization:this.state.defaultOrganization,
                selectedMainWorker: this.state.defaultMainWorker
            })
        }
        else {
            this.setState({
                selectedOrganization: event.target.value,
                selectedMainWorker:0,
                mainWorkerId:0
            });
        }
    }

    selectWorker (event){
        if(event.target.value =="" && this.state.defaultMainWorker!=""){
            this.setState({selectedMainWorker:this.state.defaultMainWorker})
        }
        else if(event.target.value == "-No select-"){
            this.setState({selectedMainWorker:0})
        }
        else {
            this.setState({selectedMainWorker: event.target.value});
        }
    }

    handleBack(){
        this.props.history.push('/workers')
    }
    handleDelete(){
        const id = this.state.id;
        axios.delete(`http://localhost:8080/workers/id/${id}`)
            .then(response => {
                console.log(response.status);
                console.log("Delete of worker successful");
                this.props.history.push('/workers');
            })
        .catch(
            error => {this.errors = [];
            console.log(error.response);
            this.setState({ message: `Worker id(${id}) cannot be deleted` });
            }
        )
    }

    validate(values) {
        let worker = {
            id: this.state.id,
            name: values.name,
            mainWorkerId: this.state.selectedMainWorker,
            organizationId: this.state.selectedOrganization
        };

        let errors = {};
        if (!values.name) {
            errors.name = 'Enter a Name'
        }
        else if(this.state.selectedOrganization==null){
            errors.name = 'Choose organization'
        }

        //это должно быть в onSubmit, но там catch не работает
        axios.put(`http://localhost:8080/workers/id/${this.state.id}`,worker)
            .then(() =>{
                this.props.history.push('/workers');
            })
            .catch(error=> {
                console.log(error.response);
                this.setState({message: 'Worker organization cannot be updated because he has got a child'})
            });

        return errors
    }

    onSubmit(values) {
        let worker = {
            id: this.state.id,
            name: values.name,
            mainWorkerId: this.state.selectedMainWorker,
            organizationId: this.state.selectedOrganization
        };

        if (this.state.id == -1) {
            WorkerDataService.createWorker(worker)
                .then(() => this.props.history.push('/workers'))
        }
    }

    render() {
        let { name, id, workers, mainWorkerId, organizations, organizationId } = this.state
        let workersList = workers.length > 0 && workers.map((wrkr, i) => {
            if(wrkr.id != this.state.id && wrkr.id != this.state.defaultMainWorker &&wrkr.organizationId == this.state.selectedOrganization){
                return (
                <option key={i+1} value={wrkr.id}>{wrkr.name}</option>
                )
            }
        }, this);
        let organizationsList = organizations.length > 0 && organizations.map((org, i) => {
            if(org.id != this.state.id && org.id !=this.state.defaultOrganization){
                return (
                <option key={i+1} value={org.id}>{org.name}</option>
                )
            }
        }, this);

        return (
            <div>
                <h3>Worker</h3>
                {this.state.message && <div className="alert alert-warning">{this.state.message}</div>}
                <div className="container">
                    <Formik
                        initialValues={{ id, name, workers, mainWorkerId, workersList, organizationsList,
                            organizations, organizationId }}
                        onSubmit={this.onSubmit}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form>
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning" />
                                    <fieldset className="form-group">
                                        <label>Id</label>
                                        <Field className="form-control" type="text" name="id" disabled />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Name</label>
                                        <Field className="form-control" type="text" name="name" />
                                        <br/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Organization</label>
                                        <Field className="form-control" type="text" name="organizationId" disabled/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <table className="tableOrganization">
                                            <tbody>
                                            <tr>
                                            <td><label>Choose organization</label></td>
                                            <td><select name ="selectOrganization" ref="workId"
                                                value={this.selectedOrganization}
                                                onChange={this.selectOrganization}
                                                >
                                                <option key={0} > {this.defaultOrganization} </option>
                                                {organizationsList}
                                                </select></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Main Worker</label>
                                        <Field className="form-control" type="text" name="mainWorkerId" disabled/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <table className="tableMainWorker">
                                            <tbody>
                                            <tr>
                                            <td><label>New main worker</label></td>
                                            <td><select name ="selectWorker" ref="orgId"
                                                value={this.selectedMainWorker}
                                                onChange={this.selectWorker}
                                                >
                                                <option key={0} > {this.defaultMainWorker} </option>
                                                <option key={1}>-No select-</option>
                                                {workersList}
                                                </select></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                       <div><button className="btn btn-success" type="submit">Save</button></div>
                                    </fieldset>
                                </Form>
                            )
                        }
                    </Formik>
                    <div>
                    <table>
                        <tbody>
                        <tr>
                            <td><button className="btn btn-warning" onClick={this.handleBack}>Back</button></td>
                            <td><button className="btn btn-warning" onClick={this.handleDelete}>Delete</button></td>
                        </tr>
                        </tbody>
                    </table>
                    </div>
                </div>
            </div>
        )
    }
}

export default SoloWorker
//module.exports = SoloWorker
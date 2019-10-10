import React, { Component } from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik';
import OrganizationDataService from '../service/OrganizationDataService.js';
import axios from 'axios';
class SoloOrganization extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            name: "",
            organizations: [],
            selectedMainOrganization:0,
            mainOrganizationId: 0,
            defaultMainOrg:0,
            message: null
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.selectOrganization = this.selectOrganization.bind(this);
        this.handleBack = this.handleBack.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }

    componentDidMount() {
        if(this.state.id){
                this.uploadOrganizations();
                if(this.state.id != -1){
                    this.uploadThisOrganization();
                }
            }
    }

    uploadOrganizations() {
        OrganizationDataService.retrieveAllOrganizations()
        .then(response => this.setState({
            organizations: response.data.content
        }))
    }

    uploadThisOrganization(){
        OrganizationDataService.retrieveOrganization(this.state.id)
            .then(response => this.setState({
                name: response.data.name,
                selectedMainOrganization: response.data.mainOrganizationId,
                mainOrganizationId: response.data.mainOrganizationName,
                defaultMainOrg:response.data.mainOrganizationId
                // childOrganization: response.data.
            }))
    }

    handleBack(){
        this.props.history.push('/organizations')
    }
    handleDelete(){
        var id = this.state.id;
        axios.delete(`http://localhost:8080/organizations/id/${id}`)
        .then(response => {
            console.log(response.status);
            console.log("Delete of organization successful");
            this.props.history.push('/organizations');
        })
        .catch(
            error => {this.errors = [];
            console.log(error.response);
            this.setState({ message: `Organization id(${id}) cannot be deleted` });
            }
        )
    }

    selectOrganization (event){
        if(event.target.value =="" && this.state.defaultMainOrg!=""){
            this.setState({selectedMainOrganization:this.state.defaultMainOrg})
        }
        else if(event.target.value == "-No select-"){
            this.setState({selectedMainOrganization:0})
        }
        else {
            this.setState({selectedMainOrganization: event.target.value});
        }
    }

    validate(values) {
        let errors = {};
        if (!values.name) {
            errors.name = 'Enter a Name'
        }
        return errors
    }


    onSubmit(values) {
        let organization = {
            id: this.state.id,
            name: values.name,
            mainOrganizationId: this.state.selectedMainOrganization
        };

        if (this.state.id == -1) {
            OrganizationDataService.createOrganization(organization)
                .then(() => this.props.history.push('/organizations'))
        } else {
            OrganizationDataService.updateOrganization(this.state.id, organization)
                .then(() => this.props.history.push('/organizations'))
        }
    }

    render() {
        let { name, id, organizations, mainOrganizationId } = this.state;
        let organizationsList = organizations.length > 0 && organizations.map((org, i) => {
            if(org.id != this.state.id && org.id !=this.state.defaultMainOrg){
                return (
                <option key={i+1} value={org.id}>{org.name}</option>
                )
            }
        }, this);

        return (
            <div>
                <h3>Organization</h3>
                {this.state.message && <div className="alert alert-warning">{this.state.message}</div>}
                <div className="container">
                    <Formik
                        initialValues={{ id, name, organizations, mainOrganizationId, organizationsList }}
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
                                        <label>Main Organization</label>
                                        <Field className="form-control" type="text" name="mainOrganizationId" disabled/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <table className="tableMainOrg">
                                            <tbody>
                                            <tr>
                                                <td><label>New main organization</label></td>
                                                <td><select name ="selectOrg" ref="orgId"
                                                        value={this.selectedMainOrganization}
                                                        onChange={this.selectOrganization}
                                                        >
                                                        <option key={0} > {this.defaultMainOrg} </option>
                                                        <option key={1}>-No select-</option>
                                                        {organizationsList}
                                                    </select>
                                                </td>
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

export default SoloOrganization
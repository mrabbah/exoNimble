import './home.scss';

import React from 'react';

import { connect } from 'react-redux';
import { Row, Col, Alert, Button, Label } from 'reactstrap';

import { getSession } from 'app/shared/reducers/authentication';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// tslint:disable-next-line:no-unused-variable
import moment from 'moment';

export interface IMyDataUpdateState {
  nbDays: string;
}
export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp, IMyDataUpdateState> {
  componentDidMount() {
    this.props.getSession();
  }

  constructor(props) {
    super(props);
    this.state = { nbDays: '' };
  }

  calculateNbDays = (event, errors, values) => {
    if (errors.length === 0) {
      const { myDataEntity } = this.props;
      const entity = {
        ...myDataEntity,
        ...values
      };
      const fd = moment(entity.firstDate);
      const ld = moment(entity.lasteDate);
      const nb = Math.abs(fd.diff(ld, 'days'));
      this.setState({
        nbDays: nb.toString()
      });
    }
  };

  render() {
    const { account, myDataEntity, nbDays } = this.props;
    const divStyle = {
      margin: '40px',
      border: '5px solid pink'
    };
    return (
      <Row>
        <Col md="9">
          <h2>Welcome, Nimble Ways!</h2>
          <p className="lead">Please enter two valid date:</p>
        </Col>
        <Col md="8">
          <AvForm model={1 === 1 ? {} : myDataEntity} onSubmit={this.calculateNbDays}>
            <AvGroup>
              <Label id="firstDateLabel" for="my-data-firstDate">
                First Date
              </Label>
              <AvField
                id="my-data-firstDate"
                type="date"
                className="form-control"
                name="firstDate"
                validate={{
                  required: { value: true, errorMessage: 'This field is required.' }
                }}
              />
            </AvGroup>
            <AvGroup>
              <Label id="lasteDateLabel" for="my-data-lasteDate">
                Laste Date
              </Label>
              <AvField
                id="my-data-lasteDate"
                type="date"
                className="form-control"
                name="lasteDate"
                validate={{
                  required: { value: true, errorMessage: 'This field is required.' }
                }}
              />
            </AvGroup>
            <Button color="primary" id="save-entity" type="submit">
              <FontAwesomeIcon icon="angle-down" />
              &nbsp; Calculate the number of days between the two dates entered
            </Button>
          </AvForm>
          <div className="divStyle">
            <Alert color="success">The number of days between the two date: {this.state.nbDays}.</Alert>
          </div>
        </Col>
        <Col md="3" className="pad">
          <span className="hipster rounded" />
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  myDataEntity: storeState.myData.entity,
  nbDays: storeState.nbDays
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);

import React, { Component } from 'react';
import logo from './logo.svg';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Form, Input, FormGroup, Container, Label} from 'reactstrap';
import momentPropTypes from 'react-moment-proptypes';
import moment from 'moment';
import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';
import {DateRangePicker} from 'react-dates';
import isAfterDay from 'react-dates/lib/utils/isAfterDay'
import isInclusivelyAfterDay from 'react-dates/lib/utils/isAfterDay'

class DatePicker extends Component {
  constructor(props) {
    super(props);
    this.state ={
      date: null,
      focused: null,
      startDate: moment('2020-03-01'),
      endDate: moment(),
      focusedInput: null,
    }
  }
  render() {
    return (
        <div id="dateRangePickerContainer">
          <Form>
            <FormGroup>
            <DateRangePicker
              startDate={this.state.startDate} // momentPropTypes.momentObj or null,
              startDateId="start_date_input" // PropTypes.string.isRequired,
              endDate={this.state.endDate} // momentPropTypes.momentObj or null,
              endDateId="end_date_input" // PropTypes.string.isRequired,
              onDatesChange={({ startDate, endDate }) => this.setState({ startDate, endDate })} // PropTypes.func.isRequired,
              focusedInput={this.state.focusedInput} // PropTypes.oneOf([START_DATE, END_DATE]) or null,
              onFocusChange={focusedInput => this.setState({ focusedInput })} // PropTypes.func.isRequired,
              isOutsideRange={(day) => isInclusivelyAfterDay(day, moment())}
              initialVisibleMonth={() => moment().subtract(1, 'month')}
              openDirection="down"
              firstDayOfWeek={1}
              displayFormat={"DD/MM/YYYY"}
            />
            <button type="button" className="btn btn-primary mb-2" id="dateRangeSubmitButton">Submit</button>
            </FormGroup>
          </Form>
        </div>
    );
  }
}

export default DatePicker;
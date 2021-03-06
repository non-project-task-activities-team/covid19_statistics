import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Form, FormGroup} from 'reactstrap';
import moment from 'moment';
import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';
import {DateRangePicker} from 'react-dates';
import isInclusivelyAfterDay from 'react-dates/lib/utils/isAfterDay'

class DatePicker extends Component {
  constructor(props) {
    super(props);
    this.state = {
      date: null,
      focused: null,
      startDate: moment('2020-03-01'),
      endDate: moment(),
      focusedInput: null,
      maxBackpresure: props.maxBackpresure
    }

    this.minBackpresureValue = 0;
    this.maxBackpresureValue = 1000000;

    this.handleChange = this.handleChange.bind(this);
  }

  handleSubmit(event) {
    event.preventDefault();
    this.props.dateRangeSubmitButtonHandler(this.state);
    this.btn.setAttribute("disabled", "disabled");
  }

  handleChange(event) {
    let maxBackpresure = event.target.value;
    if(maxBackpresure > this.minBackpresureValue && maxBackpresure <= this.maxBackpresureValue) {
      this.setState({maxBackpresure: maxBackpresure});
      this.btn.removeAttribute("disabled");
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
                onDatesChange={({ startDate, endDate }) => {
                  this.btn.removeAttribute("disabled");
                  this.setState({ startDate, endDate });
                }} // PropTypes.func.isRequired,
                focusedInput={this.state.focusedInput} // PropTypes.oneOf([START_DATE, END_DATE]) or null,
                onFocusChange={focusedInput => this.setState({ focusedInput })} // PropTypes.func.isRequired,
                isOutsideRange={(day) => isInclusivelyAfterDay(day, moment())}
                initialVisibleMonth={() => moment().subtract(1, 'month')}
                openDirection="down"
                firstDayOfWeek={1}
                displayFormat={"DD/MM/YYYY"}
              />
              <input 
                className="btn btn-light mb-2" 
                type="number" 
                id="quantity" 
                name="quantity" 
                min="1" 
                max="1000000" 
                placeholder="backpresure" 
                value={this.state.maxBackpresure}
                onChange={this.handleChange}
              />
              <button
                type="button"
                className="btn btn-primary mb-2"
                id="dateRangeSubmitButton"
                onClick={this.handleSubmit.bind(this)}
                ref={btn => { this.btn = btn; }}
              >
                  Submit
              </button>
            </FormGroup>
          </Form>
        </div>
    );
  }
}

export default DatePicker;
package com.example.usermanager.model.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements
        ConstraintValidator<DateConstraint, String> {

    private DateConstraint constraint;

    @Override
    public void initialize(DateConstraint customDate) {
        constraint = customDate;
    }

    @Override
    public boolean isValid(String dateField, ConstraintValidatorContext cxt) {
        SimpleDateFormat sdf = new SimpleDateFormat(constraint.value());
        try {
            sdf.setLenient(false);
            sdf.parse(dateField);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

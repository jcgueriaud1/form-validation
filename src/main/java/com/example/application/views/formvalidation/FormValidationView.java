package com.example.application.views.formvalidation;

import java.time.LocalDateTime;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Form Validation")
@Route(value = "", layout = MainLayout.class)
public class FormValidationView extends VerticalLayout {

    public FormValidationView() {
        createExampleFormValidationOnAttach();
        createExampleFormValidation();
        createExampleFormNoValidationOnStart();
    }

    private void createExampleFormValidationOnAttach() {
        Data data = new Data(LocalDateTime.now().minusDays(3));
        DateTimePicker dateTimePicker = new DateTimePicker();
        TextField textField = new TextField();
        Binder<Data> binder = new Binder<>();
        add(dateTimePicker, textField, new Button("Validate", e -> binder.validate()));
        binder.forField(dateTimePicker)
                .withValidator(d -> d.isAfter(LocalDateTime.now()), "Always wrong")
                .bind(Data::getDate, Data:: setDate);
        binder.forField(textField)
                .asRequired("Required")
                .bind(Data::getMessage, Data:: setMessage);
        binder.setBean(data);
        // textfield and datetime picker are valid on the client side
        // they are invalid on the server side
        addAttachListener(e -> {
            BinderValidationStatus<Data> binderValidationStatus = binder.validate();
            binderValidationStatus.getFieldValidationStatuses().stream()
                    .filter(v -> v.isError()).forEach(
                            bindingValidationStatus -> {
                                add(new Span("ATTACHED:"+bindingValidationStatus.getMessage().orElse("-")));
                            }
                    );
        });
        //binder.validate();
    }


    private void createExampleFormValidation() {

        Data data = new Data(LocalDateTime.now().minusDays(3));
        DateTimePicker dateTimePicker = new DateTimePicker();
        TextField textField = new TextField();Binder<Data> binder = new Binder<>();
        add(dateTimePicker, textField, new Button("Validate", e -> binder.validate()));
        binder.forField(dateTimePicker)
                .withValidator(d -> d.isAfter(LocalDateTime.now()), "Always wrong")
                .bind(Data::getDate, Data:: setDate);
        binder.forField(textField)
                .asRequired("Required")
                .bind(Data::getMessage, Data:: setMessage);
        binder.setBean(data);
        // textfield and datetime picker are valid on the client side
        // they are invalid on the server side
        BinderValidationStatus<Data> binderValidationStatus = binder.validate();
        binderValidationStatus.getFieldValidationStatuses().stream()
                .filter(v -> v.isError()).forEach(
                        bindingValidationStatus -> {
                            add(new Span("validate:"+bindingValidationStatus.getMessage().orElse("-")));
                        }
                );
    }


    private void createExampleFormNoValidationOnStart() {
        Data data = new Data(LocalDateTime.now().minusDays(3));
        DateTimePicker dateTimePicker = new DateTimePicker();
        TextField textField = new TextField();Binder<Data> binder = new Binder<>();
        add(dateTimePicker, textField, new Button("Validate", e -> binder.validate()));
        binder.forField(dateTimePicker)
                .withValidator(d -> d.isAfter(LocalDateTime.now()), "Always wrong")
                .bind(Data::getDate, Data:: setDate);
        binder.forField(textField)
                .asRequired("Required")
                .bind(Data::getMessage, Data:: setMessage);
        binder.setBean(data);
    }

    public static class Data {
        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        private LocalDateTime date;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;

        public Data(LocalDateTime date) {
            this.date = date;
        }
    }


}

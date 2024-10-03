package com.fancytrade.gui;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.fancytrade.MainApp;
import com.fancytrade.entities.User;
import com.fancytrade.gui.uikit.LoginForm;
import com.fancytrade.services.UserService;

public class Register extends Form {


    Label emailLabel, passwordLabel, nomLabel, prenomLabel;
    TextField
            emailTF,
            passwordTF,
            nomTF,
            prenomTF;
    PickerComponent dateNaissTF;


    Button registerButton;

    Form previous;

    public Register(Form previous) {
        super("Inscription", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    private void addGUIs() {


        Label loginLabel = new Label("Vous avez deja un compte ?");

        Button loginBtn = new Button("Login");
        loginBtn.addActionListener(l -> new LoginForm(MainApp.res).show());


        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        emailTF = new TextField();
        emailTF.setHint("Tapez le email");

        passwordLabel = new Label("Password : ");
        passwordLabel.setUIID("labelDefault");
        passwordTF = new TextField();
        passwordTF.setHint("Tapez le password");

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        prenomLabel = new Label("Prenom : ");
        prenomLabel.setUIID("labelDefault");
        prenomTF = new TextField();
        prenomTF.setHint("Tapez le prenom");
        dateNaissTF = PickerComponent.createDate(null).label("DateNaiss");


        registerButton = new Button("S'inscrire");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                emailLabel, emailTF,
                passwordLabel, passwordTF,
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                dateNaissTF,

                registerButton,
                loginLabel, loginBtn
        );

        this.addAll(container);
    }

    private void addActions() {

        registerButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().add(
                        new User(


                                emailTF.getText(),
                                "ROLE_USER",
                                passwordTF.getText(),
                                nomTF.getText(),
                                prenomTF.getText(),
                                dateNaissTF.getPicker().getDate()
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Inscription effectué avec succes", new Command("Ok"));
                    previous.showBack();
                } else if (responseCode == 203) {
                    Dialog.show("Erreur", "Email deja utilisé", new Command("Ok"));
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private boolean controleDeSaisie() {


        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir email", new Command("Ok"));
            return false;
        }


        if (passwordTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir password", new Command("Ok"));
            return false;
        }


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir nom", new Command("Ok"));
            return false;
        }


        if (prenomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir prenom", new Command("Ok"));
            return false;
        }


        if (dateNaissTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "DateNaiss vide", new Command("Ok"));
            return false;
        }


        return true;
    }
}
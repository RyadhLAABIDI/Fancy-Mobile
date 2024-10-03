package com.fancytrade.gui.back.user;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.fancytrade.entities.User;
import com.fancytrade.services.UserService;

public class AjouterUser extends Form {
    TextField emailTF;
    TextField rolesTF;
    TextField passwordTF;
    TextField nomTF;
    TextField prenomTF;
    Label emailLabel;
    Label rolesLabel;
    Label passwordLabel;
    Label nomLabel;
    Label prenomLabel;
    PickerComponent dateNaissTF;


    Button manageButton;

    Form previous;

    public AjouterUser(Form previous) {
        super("Ajouter", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {


        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        emailTF = new TextField();
        emailTF.setHint("Tapez le email");


        rolesLabel = new Label("Roles : ");
        rolesLabel.setUIID("labelDefault");
        rolesTF = new TextField();
        rolesTF.setHint("Tapez le roles");


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


        manageButton = new Button("Ajouter");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                emailLabel, emailTF,
                rolesLabel, rolesTF,
                passwordLabel, passwordTF,
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                dateNaissTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().add(
                        new User(


                                emailTF.getText(),
                                rolesTF.getText(),
                                passwordTF.getText(),
                                nomTF.getText(),
                                prenomTF.getText(),
                                dateNaissTF.getPicker().getDate()
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "User ajouté avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutUser) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Email vide", new Command("Ok"));
            return false;
        }


        if (rolesTF.getText().equals("")) {
            Dialog.show("Avertissement", "Roles vide", new Command("Ok"));
            return false;
        }


        if (passwordTF.getText().equals("")) {
            Dialog.show("Avertissement", "Password vide", new Command("Ok"));
            return false;
        }


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Nom vide", new Command("Ok"));
            return false;
        }


        if (prenomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Prenom vide", new Command("Ok"));
            return false;
        }


        if (dateNaissTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateNaiss", new Command("Ok"));
            return false;
        }


        return true;
    }
}
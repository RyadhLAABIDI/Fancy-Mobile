package com.fancytrade.gui.front.user;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.fancytrade.MainApp;
import com.fancytrade.entities.User;
import com.fancytrade.services.UserService;

public class EditProfile extends Form {


    User currentUser;

    Label emailLabel, nomLabel, prenomLabel;
    TextField emailTF, nomTF, prenomTF;
    PickerComponent dateNaissTF;

    Button editButton;

    Form previous;

    public EditProfile(Form previous) {
        super("Modifier mon profil", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    private void addGUIs() {
        currentUser = MainApp.getSession();


        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        emailTF = new TextField();
        emailTF.setHint("Tapez le email");

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        prenomLabel = new Label("Prenom : ");
        prenomLabel.setUIID("labelDefault");
        prenomTF = new TextField();
        prenomTF.setHint("Tapez le prenom");
        dateNaissTF = PickerComponent.createDate(null).label("DateNaiss");


        emailTF.setText(currentUser.getEmail());
        nomTF.setText(currentUser.getNom());
        prenomTF.setText(currentUser.getPrenom());
        dateNaissTF.getPicker().setDate(currentUser.getDateNaiss());


        editButton = new Button("Modifier");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                emailLabel, emailTF,
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                dateNaissTF,
                editButton
        );

        this.addAll(container);
    }

    private void addActions() {

        editButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().edit(
                        new User(
                                currentUser.getId(),
                                emailTF.getText(),
                                "ROLE_USER",
                                "",
                                nomTF.getText(),
                                prenomTF.getText(),
                                dateNaissTF.getPicker().getDate()

                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Profil modifié avec succes", new Command("Ok"));
                    MainApp.setSession(new User(
                            currentUser.getId(),
                            emailTF.getText(),
                            "ROLE_USER",
                            "",
                            nomTF.getText(),
                            prenomTF.getText(),
                            dateNaissTF.getPicker().getDate()
                    ));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((MyProfile) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Email vide", new Command("Ok"));
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
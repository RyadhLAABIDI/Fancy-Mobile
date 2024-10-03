package com.fancytrade.gui.back.user;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.fancytrade.entities.User;
import com.fancytrade.services.UserService;

public class ModifierUser extends Form {


    User currentUser;

    TextField emailTF;
    TextField rolesTF;
    TextField nomTF;
    TextField prenomTF;
    Label emailLabel;
    Label rolesLabel;
    Label nomLabel;
    Label prenomLabel;
    PickerComponent dateNaissTF;


    Button manageButton;

    Form previous;

    public ModifierUser(Form previous) {
        super("Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentUser = AfficherToutUser.currentUser;

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
        rolesTF.setText(currentUser.getRoles());
        nomTF.setText(currentUser.getNom());
        prenomTF.setText(currentUser.getPrenom());
        dateNaissTF.getPicker().setDate(currentUser.getDateNaiss());


        manageButton = new Button("Modifier");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                emailLabel, emailTF,
                rolesLabel, rolesTF,
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
                int responseCode = UserService.getInstance().edit(
                        new User(
                                currentUser.getId(),
                                emailTF.getText(),
                                rolesTF.getText(),
                                "",
                                nomTF.getText(),
                                prenomTF.getText(),
                                dateNaissTF.getPicker().getDate()

                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "User modifié avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de user. Code d'erreur : " + responseCode, new Command("Ok"));
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
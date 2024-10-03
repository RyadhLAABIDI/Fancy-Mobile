package com.fancytrade.gui.back.user;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.fancytrade.entities.User;
import com.fancytrade.services.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AfficherToutUser extends Form {

    Form previous;

    public static User currentUser = null;
    Button addBtn;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutUser(Form previous) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {


        addBtn = new Button("Ajouter");

        this.add(addBtn);


        ArrayList<User> listUsers = UserService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher user par Email");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (User user : listUsers) {
                if (user.getEmail().toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeUserModel(user);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listUsers.size() > 0) {
            for (User user : listUsers) {
                Component model = makeUserModel(user);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentUser = null;
            new AjouterUser(this).show();
        });

    }

    Label emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, dateNaissLabel;


    private Container makeModelWithoutButtons(User user) {
        Container userModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        userModel.setUIID("containerRounded");


        emailLabel = new Label("Email : " + user.getEmail());
        emailLabel.setUIID("labelDefault");

        rolesLabel = new Label("Roles : " + user.getRoles());
        rolesLabel.setUIID("labelDefault");

        passwordLabel = new Label("Password : " + user.getPassword());
        passwordLabel.setUIID("labelDefault");

        nomLabel = new Label("Nom : " + user.getNom());
        nomLabel.setUIID("labelDefault");

        prenomLabel = new Label("Prenom : " + user.getPrenom());
        prenomLabel.setUIID("labelDefault");

        dateNaissLabel = new Label("DateNaiss : " + new SimpleDateFormat("dd-MM-yyyy").format(user.getDateNaiss()));
        dateNaissLabel.setUIID("labelDefault");


        userModel.addAll(

                emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, dateNaissLabel
        );

        return userModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeUserModel(User user) {

        Container userModel = makeModelWithoutButtons(user);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");

        editBtn.addActionListener(action -> {
            currentUser = user;
            new ModifierUser(this).show();
        });

        deleteBtn = new Button("Supprimer");

        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce user ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = UserService.getInstance().delete(user.getId());

                if (responseCode == 200) {
                    currentUser = null;
                    dlg.dispose();
                    userModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        userModel.add(btnsContainer);

        return userModel;
    }

}
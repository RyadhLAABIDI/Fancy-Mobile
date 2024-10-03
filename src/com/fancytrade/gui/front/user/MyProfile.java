package com.fancytrade.gui.front.user;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.fancytrade.MainApp;
import com.fancytrade.entities.User;
import com.fancytrade.gui.uikit.SideMenuBaseForm;

import java.text.SimpleDateFormat;

public class MyProfile extends SideMenuBaseForm {


    public static User currentUser = null;
    Button editProfileBtn;

    public MyProfile(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Mon profil");
        getContentPane().setScrollVisible(false);

        super.addSideMenu(res);

        Image profilePic = res.getImage("user-picture.jpg");
        Image mask = res.getImage("round-mask.png");
        profilePic = profilePic.fill(mask.getWidth(), mask.getHeight());
        Label profilePicLabel = new Label(profilePic, "ProfilePicTitle");
        profilePicLabel.setMask(mask.createMask());

        Button menuButton = new Button("");
        menuButton.setUIID("Title");
        FontImage.setMaterialIcon(menuButton, FontImage.MATERIAL_MENU);
        menuButton.addActionListener(e -> getToolbar().openSideMenu());

        Container container = new Container();
        container.setPreferredH(250);
        this.add(container);

        addGUIs();
        addActions();

    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {

        Container container = new Container();
        container.setPreferredH(250);
        this.add(container);

        currentUser = MainApp.getSession();

        editProfileBtn = new Button("Modifier mon profil");

        this.add(editProfileBtn);

        this.add(makeUserModel(currentUser));
    }

    private void addActions() {
        editProfileBtn.addActionListener(action -> {
            currentUser = null;
            new EditProfile(this).show();
        });
    }

    Label emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, dateNaissLabel;


    private Component makeUserModel(User user) {
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

}
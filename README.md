# Carnet de Contacts - Java / PostgreSQL / JavaFX

## Description

Ce projet est une application desktop développée en Java avec JavaFX.  
Elle permet de gérer un carnet de contacts connecté à une base de données PostgreSQL.

L'application permet d'afficher, ajouter, modifier, supprimer et rechercher des contacts.  
Elle contient aussi un graphique qui affiche la répartition des contacts par catégorie.

Ce projet a été réalisé dans le cadre d'un TP Java / PostgreSQL / JavaFX avec Eclipse, sans Maven ni Gradle.

---

## Fonctionnalités

- Affichage des contacts dans un tableau JavaFX `TableView`
- Ajout d'un nouveau contact
- Modification d'un contact sélectionné
- Suppression d'un contact
- Effacement des champs du formulaire
- Recherche par nom ou prénom
- Affichage d'un graphique `PieChart` par catégorie
- Connexion à une base de données PostgreSQL avec JDBC

---

## Technologies utilisées

- Java 18
- JavaFX 18
- PostgreSQL
- JDBC PostgreSQL
- Eclipse IDE for Java Developers
- pgAdmin

---

## Structure du projet

```text
CarnetContacts
│
├── src
│   ├── ma.acme.contacts
│   │   ├── App.java
│   │   └── Test.java
│   │
│   ├── ma.acme.contacts.model
│   │   └── Contact.java
│   │
│   ├── ma.acme.contacts.dao
│   │   ├── Database.java
│   │   └── ContactDao.java
│   │
│   └── ma.acme.contacts.ui
│       └── MainView.java
│
├── lib
│   └── postgresql-42.7.3.jar
│
├── .classpath
├── .project
└── README.md
```

---

## Configuration de la base de données

Créer une base de données PostgreSQL nommée :

```sql
carnet_contacts
```

Ensuite, exécuter le script SQL suivant dans pgAdmin :

```sql
CREATE TABLE contact (
    id         SERIAL PRIMARY KEY,
    nom        VARCHAR(80)  NOT NULL,
    prenom     VARCHAR(80)  NOT NULL,
    email      VARCHAR(120) NOT NULL UNIQUE,
    telephone  VARCHAR(20),
    categorie  VARCHAR(30)  NOT NULL DEFAULT 'Autre'
);

INSERT INTO contact (nom, prenom, email, telephone, categorie) VALUES
  ('Dupont',  'Amina',   'amina.dupont@mail.com',  '0612345678', 'Famille'),
  ('Benali',  'Youssef', 'y.benali@mail.com',      '0623456789', 'Travail'),
  ('Laurent', 'Marie',   'marie.laurent@mail.com', '0634567890', 'Ami');
```

---

## Configuration de la connexion PostgreSQL

Dans le fichier :

```text
src/ma/acme/contacts/dao/Database.java
```

modifier les informations de connexion selon votre installation PostgreSQL :

```java
private static final String URL = "jdbc:postgresql://localhost:5432/carnet_contacts";
private static final String USER = "postgres";
private static final String PASSWORD = "VOTRE_MOT_DE_PASSE";
```

Remplacer `VOTRE_MOT_DE_PASSE` par le mot de passe PostgreSQL utilisé sur la machine.

---

## Configuration JavaFX dans Eclipse

Le projet utilise JavaFX 18.  
Il faut ajouter les fichiers `.jar` du dossier JavaFX au Build Path du projet.

Exemple de chemin JavaFX utilisé :

```text
C:\dev\javafx-sdk-18\lib
```

Dans Eclipse, ajouter aussi les arguments VM suivants pour lancer l'application :

```text
--module-path "C:\dev\javafx-sdk-18\lib" --add-modules javafx.controls
```

---

## Lancement de l'application

La classe principale à exécuter est :

```text
ma.acme.contacts.App
```

Dans Eclipse :

```text
Clic droit sur App.java
→ Run As
→ Java Application
```

---

## Test de la connexion

La classe `Test.java` permet de tester la connexion entre Java et PostgreSQL.

Elle affiche les contacts dans la console Eclipse.

Résultat attendu :

```text
Benali     Youssef    y.benali@mail.com
Dupont     Amina      amina.dupont@mail.com
Laurent    Marie      marie.laurent@mail.com
```

---

## Interface de l'application

L'application contient :

- une barre de recherche en haut ;
- un graphique de répartition par catégorie ;
- un tableau des contacts ;
- un formulaire à droite ;
- des boutons en bas pour ajouter, modifier, supprimer et effacer.

---

## Auteur

Projet réalisé par : Yassine

---

## Remarque

Ce projet est une version simple sans Maven ni Gradle.  
Les bibliothèques JavaFX et PostgreSQL JDBC sont ajoutées manuellement dans Eclipse.

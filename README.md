# FTP Server

## Authors
Durieux Thomas
Toulet Cyrille

14/02/2014

## Introduction
Ce serveur FTP a été réalisé dans le cadre du cours: "Construction d'Applications Réparties" de l'université Lille 1 à Lille (France).

Il a été réalisé en Java et implémente les principales fonctionnalités du protocole FTP.

## Architecture
### Interfaces

- FTPServer: Permet de lancer le serveur FTP
- FTPClientSocket: Permet la communication avec le socket du client
- FTPClient: Sauvegarde tous les informations qui doivent être conservé entre deux requêtes
- FTPRequestHandler: Permet d’exécuter les commandes du client
- FTPTransfertSocket: Simplifie et gère le transfert de données binaires entre le client et le serveur. Le mode Actif et Passif de FTP sont doivent être géré de manière transparente par les implémentations.

### Exceptions
- RequestHandlerException: Exception lancée par les différentes méthodes qui traitent les commandes FTP (commande non trouvée, droits non suffisant, ...).
- ServerSocketException: Exception lancée par les différents serveur (serveur permettant aux clients de se connecter et le serveur permettant de créer des connections de données en mode passif)
- SocketException: Exception lancée par les différents sockets (connexion interrompue, impossibilité de se connecter au serveur,...)

Les exceptions sont logguée le plus haut possible grâce à la classe LoggerUtilities qui permet de logguer des données à différents niveaux de priorité.

## Fonctionalités

FTP Command | Paramètre   | Description
------------|-------------|------------------------------------------------------------------
TYPE        | AEIL [NTC]  | Permet de définir le type d’encodage primaire et secondaire
USER        | username    | Défini le nom d'utilisateur du client
PASS        | password    | Défini le mot de passe du client
QUIT        |             | Ferme la connexion
SYST        |             | Indique le type de système utilisé par le serveur (UNIX Type: L8)
OPTS        | key value   | Défini des options qui peuvent être utilisé par le serveur
PWD         |             | Retourne le chemin du répertoire de travail courant
CWD         | path        | Change le répertoire du travail
CDUP        |             | Monte d'un nouveau dans l'arborescence de fichier
PORT        | a,b,c,d,i,p | Indique au serveur l'adresse du socket de données (mode actif)
PASV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif)
EPSV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif)
ABOR        |             | Interrompt le transfert de données
LIST        | [path]      | Liste le contenu d'un répertoire (données envoyées sur le socket de données)
NLST        | [path]      | Liste les nom de fichier et dossiers (données envoyées sur le socket de données)
SIZE        | path        | Retourne la taille du fichier
RETR        | path        | Récupérer le contenu du fichier (données envoyées sur le socket de données)
STOR        | path        | Sauvegarde les données envoyé sur le socket de données dans un fichier
MDTM        | path        | Récupère la date de dernière modification
MKD         | path        | Créer un nouveau dossier
RMD         | path        | Supprime un dossier
DELE        | path        | Supprime un fichier
RNFR        | path        | Indique un fichier à renommer
RNTO        | path        | Le nouveau nom du fichier à renommer

## Configuration

Ce serveur FTP possède plusieurs options présentes dans le fichier ftp_config.ini:

- defaultPort: le port par défaut
- maxConcurrentUser: le nombre maximum de clients connectés en même temps
- dataSocketTimeout: pour une utilisation future
- allowAnonymous: Active ou désactive le mode anonyme

## Code Samples

### Utilisation des annotations pour relier les commandes FTP aux méthodes de la classe FTPRequestHandler
Cette méthode est le cœur du serveur FTP elle permet d'invoquer les méthodes qui sont liés aux commandes FTP. L'avantage de cette technique est la possibilité de faire de la surcharge des méthodes pour correspondre aux différents paramètres que le client peut envoyer. Elle permet aussi de gérer de manière centralisée l'accès restreint aux personnes non connectées ou anonymes.

``` JAVA
final Method method = ...;
final FTPRequestAnnotation annotation = method.getAnnotation(FTPRequestAnnotation.class);
// si l'annotation "FTPRequestAnnotation" n'existe pas on recherche une autre méthode
if (annotation == null) { return; }
// si le nom de l'annotation ne correspond pas à la commande on recherche une autre méthode
if (!annotation.name().equals(command)) { return;}
// si le nombre d'argument de la méthode est plus important que le nombre passé avec
// la commande on recherche une autre méthode
final Class<?>[] types = method.getParameterTypes();
if (types.length > args.length) { return;}
// Génère le tableau des paramètres de la méthode
final Object[] objectArray = new Object[types.length];
for (int j = 0; j < types.length; j++) {
  ...
}

// si l'utilisateur n'est pas connecté et qu'il n'est pas en mode anonyme
// le client est prévenu qu'il n'a pas les droits d'exécuté la commande
if ((annotation.connected() && !ftpClient.isConnected())) {
  if (!FTPConfiguration.INSTANCE.getBooleanProperty("allowAnonymous")
      || !annotation.anonymous()
      || !ftpClient.getUsername().equals("anonymous")) {
        clientSocket.writeMessage("530 Not logged in.");
        return;
  }
}
// invoque la méthode
try {
  method.invoke(this, objectArray);
} catch (Exception e) {
  ...
}
```
## Comment démarer

### Utilisateurs
La liste des utilisateurs se trouve dans src/conf/db_user.ini.
Les utilisateurs sont stockés de cette manière nom_utilisateur=mot_de_passe
Exemple:
user=pass

### Démarer le server FTP
```
java -jar TP1_DURIEUX_TOULET.jar
```

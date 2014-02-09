# FTP Server

## Authors
Durieux Thomas
Toulet Cyrille
14/02/2014

## Introduction
Ce serveur FTP a été réalisé dans le cadre du cours: "Construction d'Applications Réparties" de l'université Lille 1 à Lille (France).
Ce serveur a été réalisé en Java. Ce serveur implémente les principales fonctionalités du protocole FTP.

## Architecture
- FTPServer: Permet de lancer le serveur FTP
- FTPClientSocket: Permet la communiction avec le socket du client
- FTPClient: Sauvegarde tous les informations qui doivent être conservé entre deux requêtes
- FTPRequestHandler: Permet d'éxécuter les commandes du client
- FTPTransfertServer: Permet le transfert de données binnaire entre le client et le serveur. Cette classe permet de gérer le mode Actif et Passif
- FTPTransfertClient: Permet l'écriture sur le socket de données

## Fonctionalités

FTP Command | Paramètre   | Description
------------|-------------|------------------------------------------------------------------
TYPE        | AEIL [NTC]  | Permet de définir le type d'encodate primaire et secondaire
USER        | username    | Défini le nom d'utilisateur du client
PASS        | password    | Défini le mot de passe du client
QUIT        |             | Ferme la connection 
SYST        |             | Indique le type de système utilisé par le serveur (UNIX Type: L8)
OPTS        | key value   | Défini des options qui peuvent être utilisé par le serveur
PWD         |             | Retourne le chemin du repertoire de travail courrant 
CWD         | path        | Change le répertoire du travail
CDUP        |             | Monte d'un nouveau dans l'arborescence de fichier
PORT        | a,b,c,d,i,p | Indique au serveur l'adresse du socket de données (mode actif)
PASV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif)
EPSV        |             | Demande au serveur de créer un socket de données (le port est envoyé dans la réponse) (mode passif)
ABOR        |             | Interrompt le transfert de données
LIST        | [path]      | List le contenu d'un répertoire (données envoyées sur le socket de données)
NLST        | [path]      | List les nom de fichier et dossiers (données envoyées sur le socket de données)
SIZE        | [pathFile]  | Retourne la taille du fichier
RETR        | path        | Récupérer le contenu du fichier (données envoyées sur le socket de données)
STOR        | path        | Sauvegarde les données envoyé sur le socket de données dans un fichier
MDTM        | path        | Récupère la date de dernière modification
MKD         | path        | Créer un nouveau dossier
RMD         | path        | Supprime un dossier
DELE        | path        | Supprime un fichier
RNFR        | path        | Indique un fichier à renomer
RNTO        | path        | Le nouveau nom du fichier à renomer


## Code Samples

### Utilisation des annotations pour relier les commandes FTP aux méthodes de la classe FTPRequestHandler

## Start server

/usr/lib/jvm/j2sdk1.7-oracle/bin/java -jar ftp_server.jar




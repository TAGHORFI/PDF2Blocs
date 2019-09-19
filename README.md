# PDF2Blocs

Un script java qui permet de segmenter des documents numériques au format PDF ayant un contenu textuel en français.

La segmentation consiste a organiser le contenue textuel d'un document en une liste de blocs distincts.

Ce programme est basée sur l'outil pdftohtml https://doc.ubuntu-fr.org/pdftohtml et la librairie dom4j https://dom4j.github.io/. 


Le programme prend en entrée un dossier contenant un (des) documents numérique au format PDF, et produit en sortie la version XML non ségmentée de ce (ces) documents (produite directement à partir de pdftohtml), ainsi que la version XML segmentée de ce (ces) document. Une sortie en format TXT basée sur la segmentation viendra ultérieurement.



- pdf2blocs.jar : Executable Java

	-> Entrée : dossier contenant un (des) documents au format PDF;
	
	-> Sortie : 1- la version XML du (des) documents à partir de pdftohtml. Ces fichiers seront dans le même dossier d'entrée. 2- la version XML du (des) documents à partir de pdftohtml réparée d'erreurs syntaxiques. Ces fichiers seront dans un dossier nommée "fixed". 3 - La version segmentée du (des) documents. Ces fichiers seront dans un dossier nommée "blocs".

Le script est executable en mode console en utilisant la ligne de commande suivante : 

```
java -jar pdf2blocs.jar [chemin_dossier] 

```

- src/convert.java : Code source à partir duquel l'exécutable pdf2blocs.jar a été créé.
La fonction qui permet de construire les blocs est la fonction bar. Elle prend en entrée un document XML de pdftohtml et produit un document XML segmenté en utilisant une nouvelle balise <bloc>.

- src/ressources : fichiers externes utilisé par convert.java. 
	-> src/ressources/mots_vides.txt : liste de mots vides en français ;
	-> src/ressources/ponctuation.txt : liste de caractères de ponctuation.

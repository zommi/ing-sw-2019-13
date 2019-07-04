Gruppo 13

Matteo Pacciani
Tommaso Pegolotti
Alessia Paccagnella

Funzionalità sviluppate:
    -Regole complete
    -Socket
    -RMI
    -Gui
    -CLI
    -Partite Multiple

Generare il jar:
    1)Posizionarsi nella cartella del progetto
    2)eseguire il comando : mvn clean
    3)eseguire il comando : mvn package

Come iniziare il gioco:

    Da intellij :
        1)Start server
        2)Far partire almeno 3 client sia UpdaterGui che UpdaterCli

    Jar :
        1) spostarsi nella cartella del jar (/ing-sw-2019-13/target)
        2) eseguire il comando : java -jar ing-sw-2019-13-1.0-SNAPSHOT-jar-with-dependencies.jar server
           per far partire il server
        3) eseguire il comando : java -jar ing-sw-2019-13-1.0-SNAPSHOT-jar-with-dependencies.jar gui
           per far partire un client con la gui oppure
           eseguire il comando : java -jar ing-sw-2019-13-1.0-SNAPSHOT-jar-with-dependencies.jar cli
           per far partire un client con la cli.

Per usare la cli basta riempire i campi richiesti, per far parire la gui bisogna
riempire i campi presenti nella schermata iniziale, il programma chiederà le informazioni
relative alla partita se necessario.

NB: è necessario installare javaFX per il proprio sistema operativo.
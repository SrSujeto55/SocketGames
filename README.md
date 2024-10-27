# Compilar
    //[Servidor]
    javac -d bin Server.java

    //[Cliente]
    javac -d bin Client.java

# Ejecutar 

    //[Servidor]
    java -cp bin Server [PORT]
    
    //[Cliente]
    java -cp bin Client [PORT]


Las reglas par jugar con el servidor son simples, mientras que en una shell se compila y ejecuta el servidor, en otra se pueden conectar clientes,
el servidor, para cada cliente que se conecta, manda las posibles interacciones que puede usar el cliente para jugar, cuando escribe la palabra EXIT fuera de un juego, el servidor termina su conexión y sigue con las demás.

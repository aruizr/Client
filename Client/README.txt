Obrir config.properties

Si el teu router no té els ports oberts i només vols fer una prova 
del funcionament del Servidor i Client en el mateix PC recomanem la següent configuració:

	serverAddress = (direcció IP LOCAL del PC que executará el Server)
	serverPort = 4444
	localTest = true

Si vols fer una prova del Server amb Clients connectant-se des de xarxes externes i tenint el port 4444 de router obert
recommanem la següent configuració:

	serverAddress = (direcció IP PUBLICA del PC que executará el Server)
	serverPort = 4444
	localTest = false

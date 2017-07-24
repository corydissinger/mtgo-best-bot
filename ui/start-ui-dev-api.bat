SET PATH=%PATH%;%~dp0\node
cd .\bot-ui
.\node_modules\.bin\json-server --watch db.json --port 8443 --routes routes.json
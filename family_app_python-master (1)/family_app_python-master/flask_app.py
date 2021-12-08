from flask import Flask, request, jsonify, make_response, Response
import simplejson as json
import jsonpickle
from PIL import Image
import os
from os import walk
import dropbox
import shutil
import requests
from flask_cors import CORS, cross_origin
import json


app = Flask(__name__)
env_vars = []
with open('config.env') as f:
    for line in f:
        if line.startswith('#') or not line.strip():
            continue
        key, value = line.strip().split('=', 1)
        env_vars.append({key, value})

key, value = env_vars[0]
CORS(app, support_credentials=True)
dbx = dropbox.Dropbox(key)
fileName = "test."


def upload_file(file_from, file_to):
    with open(file_from, 'rb') as f:
        dbx.files_upload(f.read(), file_to)


def folder_traverse(image, ext):
    images = dbx.files_list_folder("/family")
    on_dropbox = None
    for filesIn in images.entries:
        filename = filesIn.path_display.split('/')[-1]
        ext = filename.split('.')[-1]
        with open(fileName+ext, "wb") as f:
            metadata, res = dbx.files_download(
                path=filesIn.path_display)
            f.write(res.content)
            f.close()
        on_dropbox = Image.open(fileName+ext)
        if on_dropbox == image:
            on_dropbox.close()
            os.chmod(fileName+ext, 0o777)
            os.remove(fileName+ext)
            return {'duplicated': True}
    on_dropbox.close()
    os.chmod(fileName+ext, 0o777)
    os.remove(fileName+ext)
    return {'duplicated': False}


@ app.route("/duplication-check", methods=['POST'])
def checkDuplicatedImages():

    r = request
    uploadedImage = Image.open(r.files['imageFile'])
    ext = r.files['imageFile'].filename.split('.')[1]
    response = folder_traverse(uploadedImage, ext)
    response_pickled = jsonpickle.encode(response)
    return Response(response=response_pickled, status=200, mimetype="application/json")


app.run(host="127.0.0.1", port=8812, debug=True)

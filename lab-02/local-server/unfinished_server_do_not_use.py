from http.server import BaseHTTPRequestHandler, HTTPServer
import time

class Handler(BaseHTTPRequestHandler):

    def do_GET(self):
        if self.path == "/slow":
            time.sleep(0.1)

        self.send_response(200)
        self.send_header("Content-Type", "application/json")
        self.end_headers()

        self.wfile.write(b'{"status":"ok"}')

    def log_message(self, format, *args):
        pass


server = HTTPServer(("127.0.0.1", 8080), Handler)

print("Server running on http://127.0.0.1:8080")
server.serve_forever()

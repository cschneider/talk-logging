Sample-rest
-----------

REST service
------------

REST service that returns 200 or 418 depending on the input.

* Call http://localhost:8181/cxf/echo/tee to get 200 and your message as a reply.
* Call http://localhost:8181/cxf/echo/coffee to get 418 and the famous "I am a teapot" as reply.

Load test command
-----------------

Call ``loadtest:start <percentage-failures>``  on the shell to call the service many times and return an error
for the given percentage of messages.


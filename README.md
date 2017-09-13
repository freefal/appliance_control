# Appliance Control

Appliance Control contains both the client and server code to remotely control the power to an appliance using a raspberry pi.

### What You Need
* Raspberry Pi (e.g., https://www.raspberrypi.org/products/raspberry-pi-2-model-b/)
* A Power Cord that Switches AC Power (e.g., http://www.powerswitchtail.com/Pages/default.aspx)

### Installation

On machine running a web server of your choice:
* Add a symlink within /var/www that points to the web directory. For example:
```sh
ln -s /home/freefal/projects/appliance_control/web /var/www/my_path
```

* Checkout the code and run it
```sh
git clone https://github.com/freefal/appliance_control
ant run
```

On the Raspberry Pi:

Hook up GPIO Pin 01 to DC voltage on the Power Cord and one of the many ground pins on the Pi to DC ground on the Power Cord (see pin numbers here: http://pi4j.com/pins/model-b-plus.html)
```sh
git clone https://github.com/freefal/appliance_control
sudo ant runpiclient
```

### Thanks

* Pi4J (https://github.com/Pi4J/pi4j/)

Feel free to reach out with questions!

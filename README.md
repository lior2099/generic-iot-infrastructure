# Generic-Iot-Infrastructure

## Overview
Generic IoT infrastructure that lets a company:
- register customers and products,
- track active devices in the field,
- receive updates/telemetry,
- manage device info and behavior in a simple, central way.

The system is built like a real product:
- **Frontend** – a web interface for the customer side (register, view, track).
- **Backend** – services that manage data, messaging, and device lifecycle.

The core idea is modular design. Each major capability (thread pool, messaging, data layer, etc.) is implemented as its own component, using clean interfaces and well-known design patterns. That means you can replace/extend pieces without rewriting the whole system.

---

## Architecture at a Glance
**1. Client-facing layer (Frontend)**  
A simple web UI that lets a customer:
- sign up / log in,
- register a new product / device,
- view product status and updates.

The point is to make it easy for a non-engineer user (the “company”) to manage devices without touching the backend directly.

**2. Backend services**  
Responsible for:
- user accounts and authentication,
- product/device registration,
- storing state and telemetry,
- sending/receiving updates between devices and the platform,
- internal coordination (threads, queues, etc.).

The backend is written in Java and split into logical modules like `admin_db`, `chat`, `directory_watcher`, `thread_pool`, `waitable_pq`, etc.  
Some modules are more “product feature,” some are “infrastructure building blocks.”

---

## Data Storage
We use two different data approaches on purpose, depending on what the data is:

### Relational (SQL)
- Customer/tenant registration and account info are stored in a classic SQL-style table model.
- Why SQL? Because account data is structured, consistent, and we care about integrity (unique users, permissions, etc.).

### NoSQL
- Product/device registration and live device data are stored in a NoSQL database (e.g. MongoDB).
- Why NoSQL? Devices send flexible, evolving data (status, telemetry, heartbeat, etc.). We don’t want to redesign schemas every time firmware changes.

This split models what real IoT platforms do: stable “who are you?” info in SQL, dynamic “what is your device doing?” info in NoSQL.

---

## Main Components / Modules

### `admin_db/`
Handles company/customer registration and access control.
- Stores users in structured SQL.
- Exposes basic operations like “create customer,” “list all products owned by this customer,” etc.
- Think of it as the “account portal logic.”

### `mongoDB/` (NoSQL layer)
Responsible for registered products and their runtime data.
- New products/devices are created here with an ID and metadata.
- Device status / last-seen heartbeat / updates are written here.
- This is how you can ask: “What is device #123 doing right now?”

### `chat/`
Lightweight messaging / communication channel.
- Lets parts of the system (or simulated devices) send messages to each other.
- Used to model “device talking back to server” and “server issuing a command to device.”
- Also good for admin/support chat or debug console patterns.

### `directory_watcher/`
A filesystem watcher.
- Monitors a folder for changes (new files, updated files, etc.).
- Can be used to automatically load new device definitions, config files, firmware drops, etc.
- This mimics OTA-style workflows: ops team drops a new file, the platform reacts.

### `dynamic_jar_loader/`
Runtime module loading.
- Can load code (JARs) dynamically without restarting the whole system.
- Useful for hot-plugging new device drivers / behaviors.
- This is the “we just onboarded a new hardware product, can we support it without redeploying everything?” piece.

### `thread_pool/`
Custom thread pool implementation.
- Instead of using the default executor blindly, the system includes its own thread pool logic.
- Handles task scheduling, worker lifecycle, and parallel work safely.
- Important for scaling when you have hundreds/thousands of devices all “talking” at once.

### `waitable_pq/` (Waitable Priority Queue)
A blocking priority queue that workers can safely share.
- Tasks are prioritized (not all jobs are equal: “device offline” alert is more urgent than “daily stats”).
- Threads can wait on the queue instead of busy-looping.
- This ties directly into the thread pool to coordinate background jobs.

### `concurrency/`
Low-level concurrency utilities.
- Synchronization helpers.
- Thread-safe data structures.
- The idea is to make concurrent code predictable and reusable across the whole system.

### `vending_machine/`
Simulated device / example module.
- A mock “product” that registers itself and sends state.
- Used as a sample “real device” that the platform would manage.
- This makes the code more than just abstract infrastructure – you can actually see how a device would integrate.

---

## Design Patterns in Use
This project isn’t just “Java code.” It’s very explicitly practicing and applying classic design patterns in real contexts:

### Observer
Used when one part of the system needs to react to changes from another part.
- Example: if a device’s status changes, notify whoever is “watching” that device.
- This decouples “who raised the event” from “who cares about the event.”

### Factory
Centralized creation of objects/devices.
- Instead of manually instantiating each device type everywhere, the factory decides how to build it.
- Makes it easy to add a new product type, or swap an implementation.

### Singleton
Global managers that should only exist once (for example: a central registry, a dispatcher, or a config loader).
- Enforces “there is exactly one authority for X in this runtime.”

### Thread Pool / Work Queue
Pattern for scaling background work.
- Workers pull jobs from a queue.
- Jobs can be prioritized.
- System can grow horizontally without rewriting logic.

All of these patterns are implemented, not just talked about. The goal is: clean separation, testability, and “swap a part without killing the whole system.”

---

## Typical Flow (High Level)
1. **Customer signs up**  
   Customer is stored in SQL via `admin_db/`.

2. **Customer registers a product / device**  
   Device metadata is stored in NoSQL (MongoDB-like module).  
   Now the platform “knows” this device.

3. **Device starts sending updates**  
   Messages and telemetry go through the communication layer (`chat/`, messaging handlers).  
   The backend records status/history in the NoSQL layer.

4. **Background jobs run**  
   Worker threads from `thread_pool/` consume tasks from the `waitable_pq/`:
   - Check health.
   - Trigger alerts if device goes silent.
   - Process queued commands (like “update firmware”).

5. **Admin views dashboard**  
   Frontend shows which devices are online, last update time, etc.

---

## Tech Stack
- **Language:** Primarily Java  
- **Storage:** SQL for accounts, NoSQL for devices/telemetry  
- **Infra / Concurrency:** custom thread pool and waitable priority queue  
- **Dynamic behavior:** runtime JAR loading, directory watching  
- **Patterns:** Observer, Factory, Singleton, etc. baked into real modules

---

## Status
This is an active / evolving infrastructure project.

It’s not just a UI demo — it’s a sandbox for:
- backend systems design,
- concurrency and scheduling,
- device lifecycle management,
- data modeling for IoT at scale.

The goal is to get something close to what a real IoT backend platform looks like under the hood:  
accounts, products, live status, async work, and the ability to grow.

---

## Running the Project (placeholder)
This section depends on how you currently build/run each module. Fill in what matches your codebase:

```bash
# 1. Clone the repo
git clone https://github.com/lior2099/IOT
cd IOT

# 2. Build / compile
mvn clean package
# or:
# javac -cp src src/**/*.java

# 3. Start the core service
java -jar target/iot-platform.jar
# or run the relevant main class

# 4. (Optional) launch the simulated device (e.g. vending_machine)
java -cp target/iot-platform.jar vending_machine.Main

# Generic-Iot-Infrastructure

## Overview
Generic IoT infrastructure that lets a company:
- register customers and products,
- track active devices in the field,
- receive updates/telemetry,
- manage device info and behavior in a simple, central way.

---

## Architecture at a Glance
**1. Client-facing layer (Frontend)**  
A simple web UI that lets a customer:
- sign up / log in,
- register a new product / device,
- view product status and updates.

**2. Backend services**  
Responsible for:
- user accounts and authentication,
- product/device registration,
- storing state and telemetry,
- sending/receiving updates between devices and the platform,
- internal coordination (threads, queues, etc.).

---

## Data Storage

### Relational (SQL)
- Customer/tenant registration and account info are stored in a classic SQL-style table model.

### NoSQL
- Product/device registration and live device data are stored in a NoSQL database (e.g. MongoDB).

---

## Main Components / Modules

### `admin_db/`
Handles company/customer registration and access control.
- Stores users in structured SQL.

### `mongoDB/` (NoSQL layer)
Responsible for registered products and their runtime data.
- New products/devices are created here with an ID and metadata.

### `directory_watcher/`
A filesystem watcher.
- Monitors a folder for changes (new files, updated files, etc.).

### `dynamic_jar_loader/`
Runtime module loading.
- Can load code (JARs) dynamically without restarting the whole system.

### `thread_pool/`
Custom thread pool implementation.

### `waitable_pq/` (Waitable Priority Queue)
A blocking priority queue that workers can safely share.
- Threads can wait on the queue instead of busy-looping.

---

## Design Patterns in Use

### Observer
Used when one part of the system needs to react to changes from another part.

### Factory
Centralized creation of objects/devices.

### Singleton
Make sure that only one was made.

### Thread Pool / Work Queue
Pattern for scaling background work.

### Command
Help with encapsulation. 

### Mediator
Help with Single responsibility. 

---

## Typical Flow (High Level)
1. **Customer signs up**  
   Customer is stored in SQL via `admin_db/`.

2. **Customer registers a product / device**  
   Device metadata is stored in NoSQL (MongoDB).  
   Now the platform “knows” this device.

3. **Device starts sending updates**  
   Messages and telemetry go through the communication layer .  
   The backend records status/history in the NoSQL layer.

4. **Background jobs run**  
   Worker threads from consume tasks from the `waitable_pq/`:
 
5. **Admin views dashboard**  
   Frontend shows which devices are online, last update time, etc.

---

## Tech Stack
- **Language:** Primarily Java  
- **Storage:** SQL for accounts, NoSQL for devices/telemetry  
- **Infra / Concurrency:** custom thread pool and waitable priority queue  
- **Dynamic behavior:** runtime JAR loading, directory watching  
- **Patterns:** Observer, Factory, Singleton, etc. baked into real modules



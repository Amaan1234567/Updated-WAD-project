# Plant.io Modernization Plan
## Your Plant Selling Website - Complete Stack & Architecture

---

## ✅ What You Already Have

**Backend (Spring Boot):**
- ✅ Products Service - DONE and working with Supabase PostgreSQL
- ⏳ Orders Service - Planned
- ⏳ Users Service - Planned
- ⏳ Bestsellers Service - Planned
- ⏳ Recommendation Service - Planned

**Database:**
- ✅ Supabase PostgreSQL (cloud-hosted at `db.soqgpysuxixbzbbdhbno.supabase.co`)
- ✅ Schema: `plant_io`
- ✅ Products table implemented

**Tech Stack:**
- ✅ Spring Boot 3.4.1
- ✅ Java 21
- ✅ PostgreSQL driver
- ✅ Spring Data JPA

---

## 🎯 Recommended Complete Stack

### Frontend Stack

**Framework:** React 18 + Vite + TypeScript

**Why this choice:**
- You know React + Vite already ✓
- Fast development and hot reload ✓
- TypeScript for type safety ✓
- Perfect for SPA e-commerce ✓

**UI Framework:** shadcn/ui + Tailwind CSS

**Why this choice:**
- Modern, beautiful components perfect for plant shop
- Copy-paste components you fully control
- Tailwind CSS is industry standard
- Dark mode built-in
- Way better than IBM Carbon for public-facing sites

**State Management:**
- **TanStack Query (React Query)** - for API calls, caching, server state
- **Zustand** - for cart, UI state, user preferences

**Routing:** React Router v6

**Forms:** React Hook Form + Zod

**HTTP Client:** Axios with interceptors

**File Structure:**
```
frontend/
├── src/
│   ├── components/
│   │   ├── ui/                    # shadcn/ui components
│   │   ├── product/
│   │   │   ├── ProductCard.tsx
│   │   │   ├── ProductGrid.tsx
│   │   │   └── ProductFilters.tsx
│   │   ├── cart/
│   │   │   ├── CartDrawer.tsx
│   │   │   └── CartItem.tsx
│   │   └── layout/
│   │       ├── Header.tsx
│   │       └── Footer.tsx
│   ├── pages/
│   │   ├── Home.tsx
│   │   ├── Products.tsx           # Main shop page
│   │   ├── ProductDetail.tsx
│   │   ├── Cart.tsx
│   │   ├── Checkout.tsx
│   │   ├── Orders.tsx
│   │   └── Profile.tsx
│   ├── services/
│   │   ├── api.ts                 # Axios instance
│   │   ├── supabase.ts            # Supabase client
│   │   ├── productService.ts      # Calls your products APIs
│   │   ├── orderService.ts
│   │   └── userService.ts
│   ├── store/
│   │   ├── authStore.ts           # Zustand for auth
│   │   └── cartStore.ts           # Zustand for cart
│   ├── hooks/
│   │   ├── useProducts.ts         # React Query hook
│   │   ├── useAuth.ts
│   │   └── useCart.ts
│   └── types/
│       ├── product.ts
│       ├── order.ts
│       └── user.ts
```

---

### Backend Architecture

**Your Current Setup:** Connecting directly to cloud Supabase

**Issues with current approach:**
1. ❌ No local development (cloud DB only)
2. ❌ Hard to test without internet
3. ❌ No API Gateway (frontend calls each service directly)
4. ❌ No authentication/authorization layer
5. ❌ Services are independent but need coordination

**Recommended Architecture:**

```
┌─────────────┐
│   Frontend  │ (React + Vite on localhost:5173)
│  (Next.js)  │
└──────┬──────┘
       │
       │ HTTP/REST
       ▼
┌──────────────────┐
│   API Gateway    │ (Spring Cloud Gateway - Port 8080)
│                  │ - Routes requests
│  Authentication  │ - Validates JWT tokens
│  Rate Limiting   │ - CORS handling
│  Load Balancing  │ - Request logging
└────────┬─────────┘
         │
    ┌────┴────────────────────────┬────────────────┐
    ▼                             ▼                ▼
┌─────────────┐    ┌──────────────────┐    ┌──────────────┐
│  Products   │    │     Orders       │    │    Users     │
│  Service    │    │    Service       │    │   Service    │
│  :8081      │    │     :8082        │    │    :8083     │
└──────┬──────┘    └────────┬─────────┘    └──────┬───────┘
       │                    │                      │
    ┌──┴────────────────────┴──────────────────────┴───┐
    │                                                   │
    ▼                                                   ▼
┌─────────────┐                              ┌─────────────────┐
│ Bestsellers │                              │ Recommendations │
│  Service    │                              │     Service     │
│   :8084     │                              │      :8085      │
└──────┬──────┘                              └────────┬────────┘
       │                                              │
       └──────────────────┬───────────────────────────┘
                          ▼
                 ┌─────────────────┐
                 │    Supabase     │
                 │   PostgreSQL    │
                 │                 │
                 │ Schemas:        │
                 │ - product_svc   │
                 │ - order_svc     │
                 │ - user_svc      │
                 └─────────────────┘
```

---

## 🔑 Authentication Strategy: JUST SUPABASE

**My Strong Recommendation: Drop Clerk, Use Only Supabase Auth**

**Why:**
1. ✅ Simpler - one auth system instead of two
2. ✅ Cheaper - included in Supabase (vs $25+/month for Clerk)
3. ✅ Better integration with your PostgreSQL database
4. ✅ Row Level Security (RLS) works automatically
5. ✅ Has everything you need:
   - Email/password login ✓
   - Social logins (Google, GitHub, Facebook) ✓
   - Magic links ✓
   - JWT tokens ✓
   - Session management ✓

**What Supabase Auth Gives You:**

```typescript
// Frontend - Login with Google
const { data, error } = await supabase.auth.signInWithOAuth({
  provider: 'google'
})

// Frontend - Login with Email
const { data, error } = await supabase.auth.signInWithPassword({
  email: 'user@example.com',
  password: 'password'
})

// Frontend - Get current user
const { data: { user } } = await supabase.auth.getUser()

// Frontend - JWT token automatically included in requests
```

**Backend Integration:**

Your Spring Boot services validate the JWT token from Supabase:

```yaml
# application.yml
supabase:
  jwt:
    secret: ${SUPABASE_JWT_SECRET}  # Get from Supabase dashboard
    issuer: https://soqgpysuxixbzbbdhbno.supabase.co/auth/v1
```

Spring Security validates tokens at the API Gateway level, then passes user info to downstream services.

---

## 🗄️ Database Strategy

**Current Issue:** You're using cloud Supabase directly

**Recommended Approach:** Hybrid Development

### Option 1: Local Supabase (Best for Development) ⭐

```bash
# Install Supabase CLI
npm install -g supabase

# In your project root
cd ~/Documents/WAD-project
supabase init

# Start local Supabase (runs in Docker)
supabase start
```

**What you get:**
- PostgreSQL on `localhost:54322`
- Supabase Studio on `localhost:54323` (like pgAdmin but better)
- Auth server locally
- Storage server locally
- Realtime server locally

**Benefits:**
- ✅ Work offline
- ✅ Faster development (no network latency)
- ✅ Free local testing
- ✅ Can reset DB anytime
- ✅ Same schema as production

**Migration from Cloud to Local:**
```bash
# Pull your existing schema from cloud
supabase db pull

# This creates migration files in supabase/migrations/
# Now local DB has same schema as cloud
```

### Option 2: Keep Using Cloud Supabase

**Pros:**
- No setup needed (you're already doing this)
- Real data in one place

**Cons:**
- ❌ Need internet to develop
- ❌ Can't easily reset/test
- ❌ Risk of messing up production data
- ❌ Slower (network latency)

**My Recommendation:** Use Local Supabase for development, Cloud for production

---

## 📁 Database Schema Per Service

**Current:** Everything in one schema `plant_io`

**Better Approach:** Separate schemas per service

```sql
-- Products Service Schema
CREATE SCHEMA IF NOT EXISTS product_svc;

CREATE TABLE product_svc.products (
    product_id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_description TEXT,
    product_wiki_link VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) DEFAULT 0,
    product_type VARCHAR(50) CHECK (product_type IN ('plant','seed','supplies','tools')),
    stock_quantity INT DEFAULT 0,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders Service Schema
CREATE SCHEMA IF NOT EXISTS order_svc;

CREATE TABLE order_svc.orders (
    order_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,  -- References user_svc.users
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(50) CHECK (order_status IN ('pending','confirmed','shipped','delivered','cancelled')),
    delivery_address TEXT NOT NULL,
    payment_method VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_svc.order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES order_svc.orders(order_id),
    product_id BIGINT NOT NULL,  -- References product_svc.products
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);

-- Users Service Schema
CREATE SCHEMA IF NOT EXISTS user_svc;

CREATE TABLE user_svc.users (
    user_id BIGSERIAL PRIMARY KEY,
    auth_id UUID NOT NULL UNIQUE,  -- Links to Supabase auth.users
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255),
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(100),
    pincode VARCHAR(10),
    user_type VARCHAR(50) CHECK (user_type IN ('customer','admin','staff')) DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bestsellers Service Schema
CREATE SCHEMA IF NOT EXISTS bestseller_svc;

CREATE TABLE bestseller_svc.bestsellers (
    bestseller_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,  -- References product_svc.products
    sales_count INT DEFAULT 0,
    revenue DECIMAL(12,2) DEFAULT 0,
    rating DECIMAL(3,2) CHECK (rating BETWEEN 0 AND 5),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Recommendations Service Schema  
CREATE SCHEMA IF NOT EXISTS recommendation_svc;

CREATE TABLE recommendation_svc.recommendations (
    recommendation_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,  -- References user_svc.users
    product_id BIGINT NOT NULL,  -- References product_svc.products
    score DECIMAL(3,2) CHECK (score BETWEEN 0 AND 1),
    reason VARCHAR(100) CHECK (reason IN ('popular','trending','similar','frequently_bought_together','personalized')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Why separate schemas:**
1. ✅ Service isolation - each service owns its data
2. ✅ Independent deployment - change one service without affecting others
3. ✅ Better security - services can't access each other's tables directly
4. ✅ Easier to understand - clear boundaries

---

## 🔄 Service Communication

**Current Issue:** Services need to talk to each other

**Example Scenarios:**
- Orders Service needs product details (from Products Service)
- Bestsellers Service needs to update when orders are placed
- Recommendations Service needs user purchase history

**Solution: Two Communication Patterns**

### 1. Synchronous (REST API Calls)

Use when you need immediate response:

```
Orders Service needs product price
    ↓
Orders Service → API Gateway → Products Service
    ↓
Returns product details immediately
```

**Implementation:** Spring Cloud OpenFeign

```java
// In Orders Service
@FeignClient(name = "products-service", url = "http://localhost:8080")
public interface ProductServiceClient {
    @GetMapping("/products/{id}")
    ProductDTO getProduct(@PathVariable Long id);
}
```

### 2. Asynchronous (Message Queue)

Use when you don't need immediate response:

```
Order is placed
    ↓
Orders Service → Publishes "OrderCreated" event → RabbitMQ
    ↓
Bestsellers Service listens and updates sales count
Notification Service listens and sends confirmation email
```

**Recommended:** RabbitMQ (simpler than Kafka for your scale)

---

## 🏗️ Missing Services You Need

Based on your plant shop, you're missing:

### 1. API Gateway Service (CRITICAL)
**Why you need it:**
- Single entry point for frontend
- Handles authentication once (instead of in each service)
- CORS configuration in one place
- Rate limiting to prevent abuse
- Request/response logging

**Port:** 8080

### 2. Notification Service
**Features:**
- Order confirmation emails
- Shipping updates
- Payment receipts
- Marketing emails (optional)

**Port:** 8086

**Why separate service:**
- Sending emails is slow, shouldn't block order creation
- Can retry failed emails
- Can batch notifications

### 3. Payment Service (If not using payment gateway directly)
**Features:**
- Integration with Razorpay/Stripe
- Payment validation
- Transaction history

**Port:** 8087

---

## 📦 Updated Project Structure

```
WAD-project/
├── frontend/                          # React + Vite
│   ├── src/
│   ├── package.json
│   └── vite.config.ts
│
├── backend/
│   ├── services/
│   │   ├── gateway/                   # NEW - API Gateway
│   │   │   ├── src/
│   │   │   └── pom.xml
│   │   │
│   │   ├── products-service/          # DONE ✓
│   │   │   ├── src/
│   │   │   └── pom.xml
│   │   │
│   │   ├── orders-service/            # TODO
│   │   ├── users-service/             # TODO
│   │   ├── bestsellers-service/       # TODO
│   │   ├── recommendation-service/    # TODO
│   │   ├── notification-service/      # NEW
│   │   └── payment-service/           # NEW (optional)
│   │
│   ├── common/                        # Shared code
│   │   ├── common-dto/                # DTOs used across services
│   │   ├── common-security/           # JWT validation utilities
│   │   └── common-utils/
│   │
│   └── docker-compose.yml             # Run all services locally
│
├── supabase/                          # Supabase local setup
│   ├── migrations/                    # Database migrations
│   ├── seed.sql                       # Test data
│   └── config.toml
│
├── legacy_website/                    # Your old PHP code
│
└── docs/
    ├── API.md                         # API documentation
    └── SETUP.md                       # Setup instructions
```

---

## 🚀 Development Workflow

### Local Development Setup

**1. Start Supabase Locally**
```bash
cd ~/Documents/WAD-project
supabase start
# PostgreSQL available at localhost:54322
# Studio UI at http://localhost:54323
```

**2. Start Backend Services**
```bash
# Terminal 1 - Products Service
cd backend/products-service
mvn spring-boot:run

# Terminal 2 - Orders Service
cd backend/orders-service
mvn spring-boot:run

# Terminal 3 - API Gateway
cd backend/gateway
mvn spring-boot:run

# Or use Docker Compose (better)
cd backend
docker-compose up
```

**3. Start Frontend**
```bash
cd frontend
npm run dev
# Opens at http://localhost:5173
```

---

## 🎨 Frontend UI Framework Decision

**You asked about better options than IBM Carbon**

### My Top Recommendation: shadcn/ui + Tailwind CSS

**Why this is perfect for Plant.io:**

✅ **Beautiful, Modern Design**
- Perfect for e-commerce
- Professional look
- Customers will trust it

✅ **You Own the Code**
- Copy components into your project
- Modify however you want
- No package updates breaking your site

✅ **Built for React + Vite**
- Works perfectly with your stack
- TypeScript support built-in

✅ **Amazing Components for E-Commerce:**
- Product cards
- Shopping cart drawer
- Search with filters
- Form inputs
- Modals/dialogs
- Loading states
- Toast notifications

**Setup (5 minutes):**
```bash
# In your frontend directory
npx shadcn@latest init

# Add components as needed
npx shadcn@latest add button
npx shadcn@latest add card
npx shadcn@latest add dialog
npx shadcn@latest add input
npx shadcn@latest add select
npx shadcn@latest add badge
```

**Example - Product Card Component:**
```
Clean plant image
Product name in nice typography
Price with discount badge
"Add to Cart" button with hover effect
Rating stars
```

**Visual Style:** Modern, clean, professional - perfect for selling plants

---

### Alternative: Material-UI (MUI)

**If you prefer Material Design:**
- More components out of the box
- Familiar Google design
- Heavier bundle size
- Good for admin dashboards

**But:** shadcn/ui is better for public-facing e-commerce

---

## 🔐 Authentication Flow with Supabase

### User Registration Flow

```
1. User fills registration form
   ↓
2. Frontend → Supabase Auth → Creates auth user
   ↓
3. Frontend receives auth_id and JWT
   ↓
4. Frontend → API Gateway → Users Service
   ↓
5. Users Service creates user profile in user_svc.users table
   Links to Supabase auth via auth_id
```

### Login Flow

```
1. User enters email/password
   ↓
2. Frontend → Supabase Auth → Validates credentials
   ↓
3. Supabase returns JWT token
   ↓
4. Frontend stores JWT in localStorage
   ↓
5. All subsequent API calls include JWT in header:
   Authorization: Bearer <jwt_token>
   ↓
6. API Gateway validates JWT with Supabase secret
   ↓
7. If valid, forwards request to service with user context
```

### Protected Endpoints

```java
// In API Gateway - Spring Security config
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) {
    return http
        .csrf().disable()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/products/**").permitAll()  // Public
            .requestMatchers("/auth/**").permitAll()      // Public
            .requestMatchers("/orders/**").authenticated() // Requires JWT
            .requestMatchers("/users/**").authenticated()  // Requires JWT
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt())
        .build();
}
```

---

## 📊 Technology Decisions Summary

| Component | Your Choice | Why |
|-----------|-------------|-----|
| **Frontend Framework** | React 18 + Vite + TypeScript | You know it, fast development |
| **UI Framework** | shadcn/ui + Tailwind CSS | Modern, beautiful, perfect for e-commerce |
| **State Management** | TanStack Query + Zustand | Best practices for React |
| **Backend** | Spring Boot 3.4.1 + Java 21 | Solid, scalable, what you started |
| **API Gateway** | Spring Cloud Gateway | Standard for microservices |
| **Database** | Supabase PostgreSQL | Great features, easy to use |
| **Auth** | Supabase Auth ONLY | Simpler, cheaper, better integrated |
| **Local DB** | Supabase CLI | Offline development, faster |
| **Messaging** | RabbitMQ | Simple async communication |
| **Deployment** | Docker Compose → Kubernetes later | Easy local dev, scalable production |

---

## 📅 Implementation Timeline

### Phase 1: Foundation (Week 1-2)

**Week 1:**
- [ ] Set up Supabase locally (`supabase init && supabase start`)
- [ ] Create database schemas (product_svc, order_svc, user_svc, etc.)
- [ ] Migrate products table to product_svc schema
- [ ] Update products-service to use new schema
- [ ] Create API Gateway service skeleton
- [ ] Set up frontend with React + Vite + TypeScript

**Week 2:**
- [ ] Install shadcn/ui in frontend
- [ ] Set up Supabase Auth (enable email + Google login)
- [ ] Configure JWT validation in API Gateway
- [ ] Create login/signup UI
- [ ] Test authentication flow end-to-end

### Phase 2: Core Services (Week 3-4)

**Week 3:**
- [ ] Build Users Service
- [ ] Create users table in user_svc schema
- [ ] Implement user registration (sync with Supabase auth)
- [ ] Build user profile page in frontend
- [ ] Connect frontend to Products API via Gateway

**Week 4:**
- [ ] Build Orders Service
- [ ] Create orders and order_items tables
- [ ] Implement shopping cart in frontend (Zustand)
- [ ] Build cart UI
- [ ] Create checkout flow

### Phase 3: Enhanced Features (Week 5-6)

**Week 5:**
- [ ] Build Bestsellers Service
- [ ] Set up RabbitMQ
- [ ] Publish "OrderCreated" events
- [ ] Update bestsellers on new orders
- [ ] Create bestsellers section on homepage

**Week 6:**
- [ ] Build Recommendation Service
- [ ] Simple recommendation algorithm (popular products)
- [ ] Show recommendations on product detail pages
- [ ] Build Notification Service
- [ ] Send order confirmation emails

### Phase 4: Polish & Deploy (Week 7-8)

**Week 7:**
- [ ] Add product search and filters
- [ ] Add product images (Supabase Storage)
- [ ] Build admin panel for managing products
- [ ] Add order tracking
- [ ] Payment integration (Razorpay/Stripe)

**Week 8:**
- [ ] Testing (unit, integration, E2E)
- [ ] Set up Docker Compose for all services
- [ ] Deploy to production
- [ ] Migration from legacy PHP site

---

## 🎯 Next Steps - What to Do This Week

### Day 1-2: Set Up Local Supabase
```bash
npm install -g supabase
cd ~/Documents/WAD-project
supabase init
supabase start
```

Access Studio UI at `http://localhost:54323`

### Day 3-4: Update Products Service
Change `application.properties`:
```properties
# Before
spring.datasource.url=jdbc:postgresql://db.soqgpysuxixbzbbdhbno.supabase.co:5432/postgres?currentSchema=plant_io

# After (local)
spring.datasource.url=jdbc:postgresql://localhost:54322/postgres?currentSchema=product_svc
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Day 5-7: Create Frontend
```bash
cd ~/Documents/WAD-project
npm create vite@latest frontend -- --template react-ts
cd frontend
npm install
npx shadcn@latest init
npx shadcn@latest add button card input dialog
npm run dev
```

---

## 🤔 Questions Answered

### "Should I use Clerk + Supabase or just Supabase?"
**Answer: Just Supabase.** Simpler, cheaper, better integrated.

### "React + Vite vs Next.js?"
**Answer: React + Vite.** You know it, faster for you to build.

### "Better UI framework than Carbon?"
**Answer: shadcn/ui + Tailwind.** Modern, beautiful, perfect for e-commerce.

### "Local Supabase vs Cloud?"
**Answer: Both.** Local for development, cloud for production.

---

## 💡 Pro Tips

1. **Start Small**: Get one feature working end-to-end before adding more
2. **Use Docker Compose**: Makes running all services easy
3. **API Gateway First**: Build this before other services
4. **Test Auth Early**: Authentication bugs are annoying later
5. **Use TanStack Query**: Makes API calls so much easier
6. **Don't Over-Engineer**: Build features as you need them

---

## 📚 Learning Resources

**Supabase:**
- https://supabase.com/docs/guides/getting-started
- https://supabase.com/docs/guides/auth

**shadcn/ui:**
- https://ui.shadcn.com/docs
- https://ui.shadcn.com/examples

**Spring Cloud Gateway:**
- https://spring.io/projects/spring-cloud-gateway
- https://www.baeldung.com/spring-cloud-gateway

**TanStack Query:**
- https://tanstack.com/query/latest

---

## ✅ Your Action Plan for Tomorrow

1. Install Supabase CLI: `npm install -g supabase`
2. Start local Supabase in your project
3. Create the database schemas I showed above
4. Update your products-service to connect to local Supabase
5. Test that products-service still works

Then let me know and I'll help you with the next steps!

Would you like me to help with any specific part? For example:
- Setting up the API Gateway?
- Creating the frontend with shadcn/ui?
- Writing the database migration scripts?
- Building the Orders Service?
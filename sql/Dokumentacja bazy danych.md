# Dokumentacja bazy danych – System rezerwacji wizyt lekarskich

## 1. Opis systemu

System rezerwacji wizyt lekarskich umożliwia pacjentom rejestrację oraz umawianie wizyt w przychodni.
Obsługuje wizyty stacjonarne, teleporady, rezerwacje do specjalistów, a także możliwość zamawiania recept oraz
śledzenie historii szczepień.

System wymaga potwierdzania wizyt przez recepcję lub lekarza, co zapewnia lepszą kontrolę dostępnych terminów.

## 2. Założenia bazy danych

- **Rejestracja pacjentów online i na miejscu** – pacjent może sam utworzyć konto lub recepcjonista może go dodać do systemu.
- **Brak pełnej automatyzacji terminów** – wizyta umówiona online wymaga zatwierdzenia przez recepcję/lekarza.
- **Różne role użytkowników** – pacjenci, lekarze, recepcjoniści, administratorzy.
- **Możliwość zamawiania recept** – pacjent może poprosić o receptę bez wizyty.
- **Historia szczepień** – system przechowuje dane o obowiązkowych i dobrowolnych szczepieniach.
- **Bezpieczeństwo danych** – hasła są hashowane, a dane pacjentów są oddzielone od kont użytkowników.

## 3. Struktura bazy danych

### 1️⃣ Tabela `users` (Użytkownicy)

Przechowuje dane logowania i rolę użytkownika.

- `id` – Unikalny identyfikator użytkownika (PRIMARY KEY)
- `email` – E-mail użytkownika (unikalny)
- `password_hash` – Hasło przechowywane jako hash
- `first_name, last_name, phone_number` – Dane kontaktowe
- `role` – Rola użytkownika (`PATIENT`, `DOCTOR`, `RECEPTIONIST`, `ADMIN`)
- `created_at` – Data utworzenia konta

### 2️⃣ Tabela `patients` (Pacjenci)

Przechowuje dane pacjentów (również dzieci, z możliwością przypisania opiekuna).

- `id` – Unikalny identyfikator pacjenta
- `user_id` – Powiązanie z `users`
- `pesel` – PESEL pacjenta (unikalny)
- `birth_date` – Data urodzenia
- `guardian_id` – Opiekun pacjenta (jeśli to dziecko)
- `created_at` – Data utworzenia rekordu

### 3️⃣ Tabela `doctors` (Lekarze)

Lista lekarzy (interniści, pediatrzy, specjaliści).

- `id` – Unikalny identyfikator lekarza
- `user_id` – Powiązanie z `users`
- `specialization` – Specjalizacja lekarza
- `office_number` – Numer gabinetu
- `working_hours` – Godziny pracy w formacie JSON
- `bio` – Opis kwalifikacji lekarza, np: doświadczenie, certyfikaty

### 4️⃣ Tabela `receptionists` (Recepcjoniści)

- `id` – Unikalny identyfikator recepcjonisty
- `user_id` – Powiązanie z `users`

### 5️⃣ Tabela `appointments` (Wizyty)

Przechowuje dane o wizytach lekarskich.

- `id` – Unikalny identyfikator wizyty
- `patient_id` – Pacjent umawiający wizytę
- `doctor_id` – Lekarz przypisany do wizyty
- `appointment_type` – Typ wizyty (`STATIONARY`, `TELECONSULTATION`)
- `appointment_date` – Data i godzina wizyty
- `status` – Status wizyty (`PENDING`, `CONFIRMED`, `CANCELLED`)
- `created_at` – Data utworzenia rezerwacji

### 6️⃣ Tabela `prescriptions` (Recepty)

- `id` – Unikalny identyfikator recepty
- `patient_id` – Pacjent
- `doctor_id` – Lekarz
- `issue_date` – Data wystawienia
- `prescription_details` – Lista leków
- `status` – Status realizacji (`PENDING`, `READY`, `COMPLETED`)

### 7️⃣ Tabela `vaccinations` (Szczepienia)

- `id` – Unikalny identyfikator szczepienia
- `patient_id` – Pacjent
- `vaccine_name` – Nazwa szczepionki
- `vaccination_date` – Data szczepienia
- `is_mandatory` – Czy szczepienie jest obowiązkowe

## 4. Kluczowe relacje

✅ `users` ↔ `patients`, `doctors`, `receptionists` – użytkownicy mogą być pacjentami, lekarzami lub recepcjonistami.

✅ `patients` ↔ `appointments` – pacjent umawia wizytę.

✅ `doctors` ↔ `appointments` – wizyta przypisana do lekarza.

✅ `patients` ↔ `prescriptions` – pacjent może mieć wiele recept.

✅ `patients` ↔ `vaccinations` – historia szczepień pacjenta.

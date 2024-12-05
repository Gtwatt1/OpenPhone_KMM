import SwiftUI
import shared

struct ContentView: View {
    let viewModel = ContactsViewModel(
        contactsUseCase: ContactsUseCaseImpl(contactsRepository: ContactRepositoryImpl()),
        phoneCallUseCase: PhoneCallUseCaseImpl()
    )
    @State private var contactsState: ContactsState = .Loading()
    var body: some View {
        NavigationView {
            VStack {
                switch contactsState {
                case is ContactsState.Loading:
                    ProgressView("Loading Contacts...")
                        .progressViewStyle(CircularProgressViewStyle())
                        .padding()
                case let state as ContactsState.Success:
                    contactList(state.contacts)
                case let state as ContactsState.Error:
                    Text(state.errorMessage)
                        .foregroundColor(.red)
                        .padding()
                default:
                    EmptyView()
                }
            }
            .navigationBarTitle("OpenPhone", displayMode: .large)

        }
        .onAppear {
            viewModel.contactsState.addObserver { state in
                guard let state else { return }
                switch state {
                case is ContactsState.Loading:
                    self.contactsState = .Loading()
                case let state as ContactsState.Success:
                    self.contactsState = .Success(contacts: state.contacts)
                case let state as ContactsState.Error:
                    self.contactsState = .Error(errorMessage: state.errorMessage)
                default:
                    break
                }
            }
            viewModel.fetchContacts()
        }
    }
    
    private func contactList(_ contacts: [Contact]) -> some View {
        List(contacts, id: \.name) { contact in
            HStack {
                Circle()
                    .fill(Color.blue)
                    .frame(width: 40, height: 40)
                    .overlay(
                        Text(contact.name.prefix(1))
                            .font(.headline)
                            .foregroundColor(.white)
                    )

                VStack(alignment: .leading, spacing: 4) {
                    Text(contact.name)
                        .font(.headline)
                        .foregroundColor(.primary)
                    Text(contact.phoneNumber)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                Spacer()
                Button(action: {
                    viewModel.callContact(phoneNumber: contact.phoneNumber)
                }) {
                    Image(systemName: "phone.fill")
                        .font(.title2)
                        .foregroundColor(.green)
                }
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(Color(UIColor.systemGray6))
                    .shadow(color: Color.black.opacity(0.1), radius: 5, x: 0, y: 2)
            ).listRowSeparator(.hidden)
        }
        .listStyle(PlainListStyle())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}



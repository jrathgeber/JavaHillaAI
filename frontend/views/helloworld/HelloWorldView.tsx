import { Button } from '@hilla/react-components/Button.js';
import { Notification } from '@hilla/react-components/Notification.js';
import { TextField } from '@hilla/react-components/TextField.js';
import { HelloWorldService } from 'Frontend/generated/endpoints.js';
import ContactRecord from 'Frontend/generated/com/example/application/services/HelloWorldService/ContactRecord';
import {useEffect, useState} from 'react';
import {Grid} from "@hilla/react-components/Grid";
import {GridColumn} from "@hilla/react-components/GridColumn";


export default function HelloWorldView() {

  const [name, setName] = useState('');

  const [contacts, setContacts] = useState<ContactRecord[]>([]);
  const [selected, setSelected] = useState<ContactRecord | null | undefined>();

  useEffect(() => {
    HelloWorldService.findAllContacts().then(setContacts);
  }, []);



  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextField
          label="Your name"
          onValueChanged={(e) => {
            setName(e.detail.value);
          }}
        />
        <Button
          onClick={async () => {
            const serverResponse = await HelloWorldService.sayHello(name);
            Notification.show(serverResponse);
          }}
        >
          Say hello
        </Button>
      </section>

      <div className="p-m flex gap-m">
                  <Grid
                      items={contacts}
                      onActiveItemChanged={e => setSelected(e.detail.value)}
                      selectedItems={[selected]}>

                      <GridColumn path="firstName"/>
                      <GridColumn path="lastName"/>
                      <GridColumn path="email" autoWidth/>
                      <GridColumn path="company.name" header="Company name"/>
                  </Grid>
      </div>


    </>
  );
}




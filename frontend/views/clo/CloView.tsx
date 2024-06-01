import { Button } from '@hilla/react-components/Button.js';
import { Notification } from '@hilla/react-components/Notification.js';
import { TextField } from '@hilla/react-components/TextField.js';
import { CloService } from 'Frontend/generated/endpoints.js';
//import { useState } from 'react';
import {useEffect, useState} from 'react';
import {Grid} from "@hilla/react-components/Grid";
import {GridColumn} from "@hilla/react-components/GridColumn";

import CloRecord from 'Frontend/generated/com/example/application/services/CloService/CloRecord';

export default function CloView() {

  const [name, setName] = useState('');
  const [clos, setClos] = useState<CloRecord[]>([]);
  const [selected, setSelected] = useState<CloRecord | null | undefined>();


  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextField
          label="Your Question"
          onValueChanged={(e) => {
            setName(e.detail.value);
          }}
        />
        <Button
          onClick={async () => {
            const serverResponse = await CloService.askQuestion(name);
            Notification.show(serverResponse);
          }}
        >
          Ask your question
        </Button>
      </section>

      <div className="p-m flex gap-m">
        <Grid
            items={clos}
            onActiveItemChanged={e => setSelected(e.detail.value)}
            selectedItems={[selected]}>

            <GridColumn path="name"/>
            <GridColumn path="location" autoWidth/>

        </Grid>
      </div>



    </>
  );
}
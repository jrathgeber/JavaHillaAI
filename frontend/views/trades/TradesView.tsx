import { Button } from '@hilla/react-components/Button.js';
import { Notification } from '@hilla/react-components/Notification.js';
import { TextField } from '@hilla/react-components/TextField.js';
import { TextArea } from '@hilla/react-components/TextArea.js';
import { TradeService } from 'Frontend/generated/endpoints.js';
import {useEffect, useState} from 'react';
import {Grid} from "@hilla/react-components/Grid";
import {GridColumn} from "@hilla/react-components/GridColumn";
import CloTradeRecord from 'Frontend/generated/com/example/application/services/TradeService/CloTradeRecord';
import { Link } from 'react-router-dom'


export default function TradesView() {

  const [name, setName] = useState('');
  const [cloTrades, setCloTrades] = useState<CloTradeRecord[]>([]);
  const [selected, setSelected] = useState<CloTradeRecord | null | undefined>();

  useEffect(() => {
    TradeService.findAllCloTrades().then(setCloTrades);
  }, []);


  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextArea label="Ask a long question"

            style={{ width: '100%' }}

            onValueChanged={(e) => {
                setName(e.detail.value);
            }}

        />

        <Button
          onClick={async () => {
            const serverResponse = await TradeService.askQuestion(name);
            Notification.show(serverResponse, {
             //   duration: 0,
                theme: 'contrast',
                }
            );
            TradeService.findAllCloTrades().then(setCloTrades);
          }}
        >
          What do you want to do?
        </Button>
      </section>

      <div className="p-m flex gap-m">
        <Grid
            items={cloTrades}
            onActiveItemChanged={e => setSelected(e.detail.value)}
            selectedItems={[selected]}>
            <GridColumn path="clo"/>
            <GridColumn path="dir" autoWidth/>
            <GridColumn path="quantity" autoWidth/>
            <GridColumn path="price" autoWidth/>
            <GridColumn path="cancelled" autoWidth/>
        </Grid>
      </div>
    </>
  );
}
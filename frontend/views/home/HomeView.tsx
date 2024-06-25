import React, { useEffect, useState } from 'react';
import { ComboBox } from '@hilla/react-components/ComboBox.js';
import { HomeService } from 'Frontend/generated/endpoints.js';

export default function HomeView() {

  const [items, setItems] = useState<String[]>([]);

  useEffect(() => {
    HomeService.getCountries().then((countries) => setItems(countries));
  }, []);


  return (
    <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
      <h2>Welcome to Chat with CLO</h2>
      <p>🤗 Jason Rathgeber 🤗</p>

      <ComboBox
        allowCustomValue
        label="Model"
        helperText="Select or type a model"
        items={items}
      ></ComboBox>

    </div>
  );
}

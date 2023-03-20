from typing import Union
from fastapi import FastAPI
import uvicorn

import pandas as pd
import numpy as np
from numpy import dot
from numpy.linalg import norm 


class CBRecommend():
    def __init__(self, df):
        self.df = df
        
    def cosine_sim(self, v1,v2):
        #Adjusted to have only numbers & accounts for zero
        num_cols = len(v1) - 1
        dot_prod = np.dot(v1[:num_cols], v2[:num_cols])
        norm_prod = norm(v1[:num_cols]) * norm(v2[:num_cols])
        if norm_prod == 0:
            return 0
        return dot_prod / norm_prod
    
    def recommend(self, name, n_rec): 
        # calculate similarity of input animal_id vector 
        inputVec = self.df.loc[name].values
        self.df['sim']= self.df.apply(lambda x: self.cosine_sim(inputVec, x.values), axis=1)

        # returns top animal
        return self.df.nlargest(columns='sim',n=n_rec).to_dict()

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World"}

@app.get("/getRecommendation/{name}")
def get_recommendation(name: str):
    # constants
    PATH = r'C:\Users\devan\OneDrive\Desktop\VS code Test\DogFiltering\example_name.csv'

    # import data
    df = pd.read_csv(PATH)
    
    df.set_index('name', inplace = True)
    
    # ran on a sample as an example
    t = df.copy()
    cbr = CBRecommend(df = t)
    result = cbr.recommend(name=name, n_rec=3)
    return result



@app.get("/getValue")
def read_root1():
    # constants
    PATH = r'C:\Users\devan\Python\example_name.csv'

    # import data
    df = pd.read_csv(PATH)
    
    
    df.set_index('name', inplace = True)
    
    # ran on a sample as an example
    t = df.copy()
    cbr = CBRecommend(df = t)
    #result = cbr.recommend(animal_id = t.index[0], n_rec = 3)
    result = cbr.recommend(name = 'American Bulldog', n_rec = 3)
    return result   
/*
 * AryaTest.java
 * Copyright 2013 Patrick Meade.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pmeade.arya;

import com.google.common.collect.ImmutableMap;
import com.pmeade.arya.domain.CircReferenceA;
import com.pmeade.arya.domain.CircReferenceB;
import com.pmeade.arya.domain.CityInfo;
import com.pmeade.arya.domain.ComplicatedNull;
import com.pmeade.arya.domain.ConcreteTypes;
import com.pmeade.arya.domain.GameOfArrays;
import com.pmeade.arya.domain.GameOfLists;
import com.pmeade.arya.domain.GameOfMaps;
import com.pmeade.arya.domain.Gender;
import com.pmeade.arya.domain.MagicSword;
import com.pmeade.arya.domain.MissingBooleans;
import com.pmeade.arya.domain.MultiModel;
import com.pmeade.arya.domain.Player;
import com.pmeade.arya.domain.Point;
import com.pmeade.arya.domain.Polygon;
import com.pmeade.arya.domain.SelfReference;
import com.pmeade.arya.domain.SongInfo;
import com.pmeade.arya.domain.StarMap;
import com.pmeade.arya.domain.ThroneProgram;
import com.pmeade.arya.domain.ThroneProgram2;
import com.pmeade.arya.domain.ThroneProgram3;
import com.pmeade.arya.domain.TopTen;
import com.pmeade.arya.domain.TourSchedule;
import com.pmeade.arya.domain.Weapon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class AryaTest
{
    private Arya arya;
    
    public AryaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        arya = new Arya(Arya.COMPACT_PRINTING);
        // just to see what's going on
//        arya.register(new AryaOutput() {
//            public <T> void output(T t, UUID uuid, String json) {
//                System.err.println("T:" + t);
//                System.err.println("UUID:" + uuid);
//                System.err.println(json);
//            }
//        });
        // to help build up test cases
//        arya.register(new AryaOutput() {
//            public <T> void output(T t, UUID uuid, String json) {
//                StringBuilder sb = new StringBuilder(".put(\"");
//                sb.append(uuid.toString());
//                sb.append("\",\"");
//                sb.append(json.replace("\"", "\\\""));
//                sb.append("\")");
//                System.err.println(sb.toString());
//            }
//        });
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAlwaysSucceed() {
        assertTrue(true);
    }
    
    @Test
    public void testAryaSetup() {
        assertNotNull(arya);
    }

    @Test
    public void testMockOutput() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        replay(mockOutput);
        
        Point p = new Point(1.0, 2.0, 3.0);
        arya.save(p);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveSelfReference() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        mockOutput.output(isA(SelfReference.class), isA(UUID.class), isA(String.class));
        replay(mockOutput);
        
        SelfReference sr = new SelfReference();
        arya.save(sr);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveCircularReference() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        mockOutput.output(isA(CircReferenceB.class), isA(UUID.class), isA(String.class));
        mockOutput.output(isA(CircReferenceA.class), isA(UUID.class), isA(String.class));
        replay(mockOutput);
        
        CircReferenceA cra = new CircReferenceA();
        CircReferenceB crb = new CircReferenceB();
        cra.setCircReferenceB(crb);
        crb.setCircReferenceA(cra);
        arya.save(cra);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveComplicatedNull() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        mockOutput.output(isA(ComplicatedNull.class), isA(UUID.class), isA(String.class));
        replay(mockOutput);
        
        ComplicatedNull cn = new ComplicatedNull();
        arya.save(cn);
        verify(mockOutput);
    }
    
    @Test
    public void testSavePolygon() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        mockOutput.output(isA(Polygon.class), isA(UUID.class), isA(String.class));
        replay(mockOutput);
        
        Polygon polygon = new Polygon();
        arya.save(polygon);
        verify(mockOutput);
    }

    @Test
    public void testSaveMultiModel() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(40);
        mockOutput.output(isA(Polygon.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(8);
        mockOutput.output(isA(MultiModel.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        MultiModel multiModel = new MultiModel();
        arya.save(multiModel);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveStarMap() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(Point.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(5);
        mockOutput.output(isA(StarMap.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        StarMap starMap = new StarMap();
        arya.save(starMap);
        verify(mockOutput);
    }

    @Test
    public void testSaveThroneProgram() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(ThroneProgram.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        ThroneProgram throneProgram = new ThroneProgram();
        arya.save(throneProgram);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveTourSchedule() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(TourSchedule.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        TourSchedule tourSchedule = new TourSchedule();
        arya.save(tourSchedule);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveCityInfo() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(CityInfo.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        CityInfo cityInfo = new CityInfo();
        arya.save(cityInfo);
        verify(mockOutput);
    }
    
    @Test
    public void testSaveThroneProgram2() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(ThroneProgram2.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        ThroneProgram2 throneProgram = new ThroneProgram2();
        arya.save(throneProgram);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadPoint() {
        AryaInput mapInput = new AryaInput() {
            public <T> String input(UUID uuid, Class<T> type) {
                return null;
            }
        };
        arya.register(mapInput);
        final UUID POINT_UUID = UUID.fromString("bb3d1b2b-39be-4ae3-a814-f5aec7f32f30");
        AryaInput mockInput = createMock(AryaInput.class);
        arya.register(mockInput);
        expect(mockInput.input(POINT_UUID, Point.class)).andReturn("{}");
        replay(mockInput);

        Point p = arya.load(POINT_UUID, Point.class);
        assertNotNull(p);
        assertTrue(p instanceof Point);
    }

    @Test
    public void testLoadPoint2() {
        final UUID POINT_UUID = UUID.fromString("bb3d1b2b-39be-4ae3-a814-f5aec7f32f30");
        AryaInput mockInput = createMock(AryaInput.class);
        arya.register(mockInput);
        expect(mockInput.input(POINT_UUID, Point.class)).andReturn("{ \"x\": 1.0, \"y\": 2.0, \"z\": 3.0 }");
        replay(mockInput);
        
        Point p = arya.load(POINT_UUID, Point.class);
        assertNotNull(p);
        assertTrue(p instanceof Point);
        assertEquals(1.0, p.getX(), 0.01);
        assertEquals(2.0, p.getY(), 0.01);
        assertEquals(3.0, p.getZ(), 0.01);
        verify(mockInput);
    }
    
    @Test
    public void testLoadSelfReference() {
        final UUID OBJ_UUID = UUID.fromString("0a2ac37d-5095-4d40-a85c-013a14f80635");
        AryaInput mockInput = createMock(AryaInput.class);
        arya.register(mockInput);
        expect(mockInput.input(OBJ_UUID, SelfReference.class)).andReturn("{ \"self\": \"0a2ac37d-5095-4d40-a85c-013a14f80635\" }");
        replay(mockInput);

        SelfReference p = arya.load(OBJ_UUID, SelfReference.class);
        assertNotNull(p);
        assertTrue(p instanceof SelfReference);
        assertEquals(p, p.getSelf());
        verify(mockInput);
    }
    
    @Test
    public void testLoadCircularReference() {
        final UUID OBJ1_UUID = UUID.fromString("9d0b9dbf-e8f0-450e-b693-0971f10074da");
        final UUID OBJ2_UUID = UUID.fromString("0b1aec1b-7ce3-46f8-9141-247e43ed9f50");
        AryaInput mockInput = createMock(AryaInput.class);
        arya.register(mockInput);
        expect(mockInput.input(OBJ2_UUID, CircReferenceA.class)).andReturn("{ \"circReferenceB\": \"9d0b9dbf-e8f0-450e-b693-0971f10074da\" }");
        expect(mockInput.input(OBJ1_UUID, CircReferenceB.class)).andReturn("{ \"circReferenceA\": \"0b1aec1b-7ce3-46f8-9141-247e43ed9f50\" }");
        
        replay(mockInput);

        CircReferenceA a = arya.load(OBJ2_UUID, CircReferenceA.class);
        assertNotNull(a);
        CircReferenceB b = a.getCircReferenceB();
        assertNotNull(b);
        assertTrue(a instanceof CircReferenceA);
        assertTrue(b instanceof CircReferenceB);
        assertEquals(a, b.getCircReferenceA());
        assertEquals(b, a.getCircReferenceB());
        verify(mockInput);
    }
    
    @Test
    public void testLoadCircularReference2() {
        final UUID OBJ1_UUID = UUID.fromString("9d0b9dbf-e8f0-450e-b693-0971f10074da");
        final UUID OBJ2_UUID = UUID.fromString("0b1aec1b-7ce3-46f8-9141-247e43ed9f50");
        AryaInput mockInput = createMock(AryaInput.class);
        arya.register(mockInput);
        expect(mockInput.input(OBJ1_UUID, CircReferenceB.class)).andReturn("{ \"circReferenceA\": \"0b1aec1b-7ce3-46f8-9141-247e43ed9f50\" }");
        expect(mockInput.input(OBJ2_UUID, CircReferenceA.class)).andReturn("{ \"circReferenceB\": \"9d0b9dbf-e8f0-450e-b693-0971f10074da\" }");
        
        replay(mockInput);

        CircReferenceB b = arya.load(OBJ1_UUID, CircReferenceB.class);
        assertNotNull(b);
        CircReferenceA a = b.getCircReferenceA();
        assertNotNull(a);
        assertTrue(a instanceof CircReferenceA);
        assertTrue(b instanceof CircReferenceB);
        assertEquals(a, b.getCircReferenceA());
        assertEquals(b, a.getCircReferenceB());
        verify(mockInput);
    }
    
    @Test
    public void testLoadComplicatedNull() {
        final UUID COMPLEX_NULL_UUID = UUID.fromString("bb3d1b2b-39be-4ae3-a814-f5aec7f32f30");
        AryaInput mockInput = createMock(AryaInput.class);
        arya.register(mockInput);
        expect(mockInput.input(COMPLEX_NULL_UUID, ComplicatedNull.class)).andReturn("{ \"b1\": false, \"b3\": 0, \"c1\": \"\\u0000\", \"s1\": 0, \"i1\": 0, \"l1\": 0, \"f1\": 0.0, \"d1\": 0.0 }");
        replay(mockInput);
        
        ComplicatedNull cn = arya.load(COMPLEX_NULL_UUID, ComplicatedNull.class);
        assertNotNull(cn);
        assertTrue(cn instanceof ComplicatedNull);
        verify(mockInput);
    }
    
    @Test
    public void testLoadPolygon() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("33d8877f-d09e-47ac-af55-aa7ba5994dcd", "{ \"points\": [ \"2360b219-e736-4229-937c-36569317c53b\", \"636c508f-ea90-41bf-9250-501a1d3169a8\", \"5ff05b74-d7d0-497b-88d9-81411f487deb\", \"3a4b9699-8ddb-47d5-b2c3-94f959905df2\", \"b4df1ed2-510d-44e4-bf35-1e57f7fc08a7\" ] }")
                    .put("2360b219-e736-4229-937c-36569317c53b", "{ \"x\": 0.5744826852293886, \"y\": 0.9225648677655743, \"z\": 0.6569996592595303 }")
                    .put("636c508f-ea90-41bf-9250-501a1d3169a8", "{ \"x\": 0.2857122306745653, \"y\": 0.4534172745439432, \"z\": 0.663863654078138 }")
                    .put("5ff05b74-d7d0-497b-88d9-81411f487deb", "{ \"x\": 0.20810369790377647, \"y\": 0.5696870268722891, \"z\": 0.8292937641444449 }")
                    .put("3a4b9699-8ddb-47d5-b2c3-94f959905df2", "{ \"x\": 0.8640544795187776, \"y\": 0.4372197511643232, \"z\": 0.10045860043397603 }")
                    .put("b4df1ed2-510d-44e4-bf35-1e57f7fc08a7", "{ \"x\": 0.7317833167467228, \"y\": 0.6660355291528525, \"z\": 0.7856072045675854 }")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        Polygon polygon = arya.load(UUID.fromString("33d8877f-d09e-47ac-af55-aa7ba5994dcd"), Polygon.class);
        assertNotNull(polygon);
        assertTrue(polygon instanceof Polygon);
        assertNotNull(polygon.getPoints());
        assertEquals(5, polygon.getPoints().length);
        for(Point point : polygon.getPoints()) {
            assertNotNull(point);
            assertTrue(point.getX() > 0.0);
            assertTrue(point.getY() > 0.0);
            assertTrue(point.getZ() > 0.0);
        }
    }

    @Test
    public void testLoadMultiModel() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("c7d0438d-26d0-4af5-9dbf-681f8cee49d3","{\"x\":0.021099571192649602,\"y\":0.7036413226341777,\"z\":0.5983433485963143}")
                    .put("2b11c702-d6d7-4cf7-8414-2bf28667e854","{\"x\":0.6303343651846482,\"y\":0.5231287882427204,\"z\":0.4818597882939355}")
                    .put("7d24418c-3434-47da-9b5b-0e5fbe4d7132","{\"x\":0.7429942785819678,\"y\":0.5593815983704601,\"z\":0.5291697802465374}")
                    .put("a6fadc41-e076-4155-9e28-df4cdd001439","{\"x\":0.8976032613458891,\"y\":0.7590892898415351,\"z\":0.2211419290887482}")
                    .put("5e4bd98d-7028-4edc-8571-be5be993f365","{\"x\":0.010884603490133538,\"y\":0.29975206729095083,\"z\":0.8110290912380884}")
                    .put("4b6d31b5-7d4a-4797-ac74-49b271f42313","{\"points\":[\"c7d0438d-26d0-4af5-9dbf-681f8cee49d3\",\"2b11c702-d6d7-4cf7-8414-2bf28667e854\",\"7d24418c-3434-47da-9b5b-0e5fbe4d7132\",\"a6fadc41-e076-4155-9e28-df4cdd001439\",\"5e4bd98d-7028-4edc-8571-be5be993f365\"]}")
                    .put("c03a1012-4d82-4424-92e5-79373f635c20","{\"x\":0.4168302104269732,\"y\":0.4938726425835205,\"z\":0.9971016151541567}")
                    .put("f7635a75-977f-477f-8eb7-c293f5e338f1","{\"x\":0.5006641271103947,\"y\":0.43669396207982514,\"z\":0.7581758889660456}")
                    .put("790f61e0-8d33-4a2d-8dc1-a048d176f0fb","{\"x\":0.5421331510041021,\"y\":0.05373731476749388,\"z\":0.36747569495515275}")
                    .put("95e8c0dd-a178-446c-85bb-b916be147b27","{\"x\":0.16298004026402935,\"y\":0.4972644975084719,\"z\":0.31565777248900406}")
                    .put("22dfc8a3-992d-4343-ae57-a70578e4b181","{\"x\":0.26683106151340497,\"y\":0.40840862533368705,\"z\":0.053362378578782343}")
                    .put("edb50a80-85a9-4ac5-ab0f-90e45bb86104","{\"points\":[\"c03a1012-4d82-4424-92e5-79373f635c20\",\"f7635a75-977f-477f-8eb7-c293f5e338f1\",\"790f61e0-8d33-4a2d-8dc1-a048d176f0fb\",\"95e8c0dd-a178-446c-85bb-b916be147b27\",\"22dfc8a3-992d-4343-ae57-a70578e4b181\"]}")
                    .put("f839124c-0f32-42e7-9953-0816122a0a0a","{\"x\":0.024287260175944403,\"y\":0.576925658569871,\"z\":0.7084391748904184}")
                    .put("42d37a7f-952d-4958-8716-5095252e819c","{\"x\":0.2892430643027827,\"y\":0.3166793713897601,\"z\":0.02150360026671916}")
                    .put("80f63f9b-0864-4ef3-af82-5b56d0a72bfa","{\"x\":0.41635443010236517,\"y\":0.43608344639255037,\"z\":0.32221848029541156}")
                    .put("80a24487-a551-40b6-bb1c-e8725876d2af","{\"x\":0.7590416950483105,\"y\":0.11777292813895479,\"z\":0.24211010437735858}")
                    .put("7987e57d-f781-4896-b287-e329827bccc0","{\"x\":0.9963642637375096,\"y\":0.7491725328184369,\"z\":0.08618791037264262}")
                    .put("fe59e20b-1cff-4a58-9c7d-e2f382b4f7c9","{\"points\":[\"f839124c-0f32-42e7-9953-0816122a0a0a\",\"42d37a7f-952d-4958-8716-5095252e819c\",\"80f63f9b-0864-4ef3-af82-5b56d0a72bfa\",\"80a24487-a551-40b6-bb1c-e8725876d2af\",\"7987e57d-f781-4896-b287-e329827bccc0\"]}")
                    .put("69347382-b2ee-4bcb-9742-3576f2dabc09","{\"x\":0.4206290454115552,\"y\":0.05721459183855859,\"z\":0.1303813697832752}")
                    .put("14965df6-4d82-4b91-8cc1-7535ec01804c","{\"x\":0.7820744068919518,\"y\":0.20840387897103507,\"z\":0.0013579138650712252}")
                    .put("79e61ee2-2eff-4a28-baf3-3747447a7491","{\"x\":0.9040504565046145,\"y\":0.7690088319628514,\"z\":0.26158644377262086}")
                    .put("6a86599f-93c4-4c51-a1ec-ea8d22016546","{\"x\":0.6959937840911085,\"y\":0.37324627536295163,\"z\":0.018877071574205373}")
                    .put("f93b7014-b45b-472c-bc2b-038cd14ca79d","{\"x\":0.4728479107017245,\"y\":0.9710582012030146,\"z\":0.5942143149134919}")
                    .put("a231ae49-6c1b-4f74-8f78-1006993c50e7","{\"points\":[\"69347382-b2ee-4bcb-9742-3576f2dabc09\",\"14965df6-4d82-4b91-8cc1-7535ec01804c\",\"79e61ee2-2eff-4a28-baf3-3747447a7491\",\"6a86599f-93c4-4c51-a1ec-ea8d22016546\",\"f93b7014-b45b-472c-bc2b-038cd14ca79d\"]}")
                    .put("d5c792fb-7e40-4299-82f5-54bf2d676aa8","{\"x\":0.08313040107561998,\"y\":0.806054746833847,\"z\":0.9680980803641002}")
                    .put("46dd48d7-ec97-44a3-9d09-6a952c9158d5","{\"x\":0.5643671000675576,\"y\":0.018181462592988695,\"z\":0.9137786631747541}")
                    .put("a906fb62-ff0e-42ef-bfbf-429f712c165d","{\"x\":0.7418971248471816,\"y\":0.9926208956884699,\"z\":0.27041482427792607}")
                    .put("d130b9a2-8bc1-43a8-97dc-2baa84c73fa4","{\"x\":0.1281253604234156,\"y\":0.7563629882974462,\"z\":0.2762573700543398}")
                    .put("b5dd2362-c0bc-4d6e-8162-b1318655c68d","{\"x\":0.9499200501456383,\"y\":0.39681171493936995,\"z\":0.46794358976033057}")
                    .put("8711d1e1-cd5b-4646-9635-d59766dfd0a8","{\"points\":[\"d5c792fb-7e40-4299-82f5-54bf2d676aa8\",\"46dd48d7-ec97-44a3-9d09-6a952c9158d5\",\"a906fb62-ff0e-42ef-bfbf-429f712c165d\",\"d130b9a2-8bc1-43a8-97dc-2baa84c73fa4\",\"b5dd2362-c0bc-4d6e-8162-b1318655c68d\"]}")
                    .put("73d31933-4c34-4f6c-a39a-2f71e528b2a3","{\"x\":0.7430564277455118,\"y\":0.05193764411902735,\"z\":0.874116918520681}")
                    .put("e6ac289c-f2eb-4c89-a12f-e9f19459f8c3","{\"x\":0.14784840397111632,\"y\":0.30826679957077474,\"z\":0.22114122974456418}")
                    .put("8f175fc9-9e0d-4a5e-9167-50c2911091f6","{\"x\":0.17027801871567683,\"y\":0.45860787320596974,\"z\":0.4430515270318506}")
                    .put("12b5db85-e948-4fe0-974b-710909f38078","{\"x\":0.5962618596975926,\"y\":0.713095716811496,\"z\":0.071012353761297}")
                    .put("1a5872e1-65c1-4a3f-9067-c8fe6c9dada7","{\"x\":0.7543655001855057,\"y\":0.12119384716268544,\"z\":0.281961587219836}")
                    .put("b0c538a8-334a-46a6-94d2-027777ec16f9","{\"points\":[\"73d31933-4c34-4f6c-a39a-2f71e528b2a3\",\"e6ac289c-f2eb-4c89-a12f-e9f19459f8c3\",\"8f175fc9-9e0d-4a5e-9167-50c2911091f6\",\"12b5db85-e948-4fe0-974b-710909f38078\",\"1a5872e1-65c1-4a3f-9067-c8fe6c9dada7\"]}")
                    .put("14e673e6-b836-4a8b-b642-a704e00c0dca","{\"x\":0.784229729743408,\"y\":0.5357926393700512,\"z\":0.4636031134355453}")
                    .put("da68adf5-8b95-4869-9f87-bddc504ef2df","{\"x\":0.5372132486279788,\"y\":0.1949084612495915,\"z\":0.36469066137970263}")
                    .put("f3779d8a-bc56-4a36-af0e-63fdce0810d9","{\"x\":0.34319344651248085,\"y\":0.2866693843965028,\"z\":0.9503308153384739}")
                    .put("87cb5c05-700a-412f-ab72-66e76004894e","{\"x\":0.19306149028463315,\"y\":0.6391672737449097,\"z\":0.30639157924086635}")
                    .put("ee83f18a-64f8-4af7-90b8-3301c4cb080f","{\"x\":0.016188790177836587,\"y\":0.40788632018902415,\"z\":0.12005155810642754}")
                    .put("df08bb31-4429-4730-a247-a05a87d08cd1","{\"points\":[\"14e673e6-b836-4a8b-b642-a704e00c0dca\",\"da68adf5-8b95-4869-9f87-bddc504ef2df\",\"f3779d8a-bc56-4a36-af0e-63fdce0810d9\",\"87cb5c05-700a-412f-ab72-66e76004894e\",\"ee83f18a-64f8-4af7-90b8-3301c4cb080f\"]}")
                    .put("3f6abccd-545a-4a76-b2c2-092cfddbb3fa","{\"x\":0.7641653668833556,\"y\":0.19131305364280193,\"z\":0.021233384881496176}")
                    .put("79fa8d32-344a-4c45-9685-c91b17136c94","{\"x\":0.01894168739459179,\"y\":0.6818884902907872,\"z\":0.3096929880095053}")
                    .put("8a3708c2-e7d3-4c66-9e64-3b0b5f0450e8","{\"x\":0.4912682718231295,\"y\":0.963349093152148,\"z\":0.41699917884521587}")
                    .put("224c99ae-c138-4625-b5f1-2c32fc887876","{\"x\":0.18438393046994594,\"y\":0.6723660092383855,\"z\":0.9612212449988673}")
                    .put("9bd521e4-38c4-4a14-aa28-357ea7a58376","{\"x\":0.8432729291609251,\"y\":0.3684051352211086,\"z\":0.045844522886077876}")
                    .put("a7c37f94-5395-4972-91dd-4f239844e6ab","{\"points\":[\"3f6abccd-545a-4a76-b2c2-092cfddbb3fa\",\"79fa8d32-344a-4c45-9685-c91b17136c94\",\"8a3708c2-e7d3-4c66-9e64-3b0b5f0450e8\",\"224c99ae-c138-4625-b5f1-2c32fc887876\",\"9bd521e4-38c4-4a14-aa28-357ea7a58376\"]}")
                    .put("46c7ab6b-f90a-44dd-a339-c7d99827d56c","{\"polygons\":[[[\"4b6d31b5-7d4a-4797-ac74-49b271f42313\",\"edb50a80-85a9-4ac5-ab0f-90e45bb86104\"],[\"fe59e20b-1cff-4a58-9c7d-e2f382b4f7c9\",\"a231ae49-6c1b-4f74-8f78-1006993c50e7\"]],[[\"8711d1e1-cd5b-4646-9635-d59766dfd0a8\",\"b0c538a8-334a-46a6-94d2-027777ec16f9\"],[\"df08bb31-4429-4730-a247-a05a87d08cd1\",\"a7c37f94-5395-4972-91dd-4f239844e6ab\"]]]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        MultiModel multiModel = arya.load(UUID.fromString("46c7ab6b-f90a-44dd-a339-c7d99827d56c"), MultiModel.class);
        assertNotNull(multiModel);
        assertTrue(multiModel instanceof MultiModel);
        assertNotNull(multiModel.getPolygons());
        assertEquals(2, multiModel.getPolygons().length);
        int pointCount = 0;
        for(Polygon[][] polygonss : multiModel.getPolygons()) {
            for(Polygon[] polygons : polygonss) {
                for(Polygon polygon : polygons) {
                    for(Point point : polygon.getPoints()) {
                        assertNotNull(point);
                        assertTrue(point.getX() > 0.0);
                        assertTrue(point.getY() > 0.0);
                        assertTrue(point.getZ() > 0.0);
                        pointCount++;
                    }
                }
            }
        }
        assertEquals(40, pointCount);
    }
    
    @Test
    public void testLoadStarMap() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("bea2072e-fccf-4ffe-b4da-a57c47ace755","{\"x\":0.2959620616076306,\"y\":0.3805367944863848,\"z\":0.5516316918657457}")
                    .put("94b28958-1ef0-4dc0-a941-46881884f9bb","{\"x\":0.6611414930082842,\"y\":0.9462070436703103,\"z\":0.533828552507101}")
                    .put("bc763a65-e17d-4514-bde7-d4b85851ea57","{\"x\":0.5425460728551027,\"y\":0.6980555695321307,\"z\":0.9442659849762334}")
                    .put("6ccaad00-aaa3-4e8c-aaa4-dc1bf653b366","{\"x\":0.13223451350570437,\"y\":0.947946329162634,\"z\":0.861573768703529}")
                    .put("f3f78361-8629-4f5f-b93e-41780f037bc5","{\"x\":0.7233040448574245,\"y\":0.6147269660546778,\"z\":0.25669777866569166}")
                    .put("218019e2-b495-45a0-a6df-15c33534d52b","{\"stars\":[\"bea2072e-fccf-4ffe-b4da-a57c47ace755\",\"94b28958-1ef0-4dc0-a941-46881884f9bb\",\"bc763a65-e17d-4514-bde7-d4b85851ea57\",\"6ccaad00-aaa3-4e8c-aaa4-dc1bf653b366\",\"f3f78361-8629-4f5f-b93e-41780f037bc5\"]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        StarMap starMap = arya.load(UUID.fromString("218019e2-b495-45a0-a6df-15c33534d52b"), StarMap.class);
        assertNotNull(starMap);
        assertTrue(starMap instanceof StarMap);
        assertNotNull(starMap.getStars());
        assertEquals(5, starMap.getStars().size());
        for(Point point : starMap.getStars()) {
            assertNotNull(point);
            assertTrue(point.getX() > 0.0);
            assertTrue(point.getY() > 0.0);
            assertTrue(point.getZ() > 0.0);
        }
    }
    
    @Test
    public void testLoadThroneProgram() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("b52e150e-f28c-4b43-aa1c-3208bf37e481","{\"names\":{\"Robb\":\"Stark\",\"Tywin\":\"Lannister\",\"Balon\":\"Greyjoy\",\"Myrcella\":\"Lannister\",\"Tyrion\":\"Lannister\",\"Loras\":\"Tyrell\",\"Eddard\":\"Stark\",\"Arya\":\"Stark\",\"Theon\":\"Greyjoy\",\"Mace\":\"Tyrell\",\"Catelyn\":\"Stark\",\"Tommen\":\"Lannister\",\"Jeoffery\":\"Lannister\",\"Jamie\":\"Lannister\",\"Brandon\":\"Stark\",\"Rickon\":\"Stark\",\"Sansa\":\"Stark\",\"Cersei\":\"Lannister\",\"Margaery\":\"Tyrell\",\"Asha\":\"Greyjoy\"}}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        ThroneProgram throneProgram = arya.load(UUID.fromString("b52e150e-f28c-4b43-aa1c-3208bf37e481"), ThroneProgram.class);
        assertNotNull(throneProgram);
        assertTrue(throneProgram instanceof ThroneProgram);
        assertNotNull(throneProgram.getNames());
        assertEquals(20, throneProgram.getNames().keySet().size());
        for(String name : throneProgram.getNames().keySet()) {
            String house = throneProgram.getNames().get(name);
            assertNotNull(house);
            assertTrue(
                (house.equals(ThroneProgram.GREYJOY))
             || (house.equals(ThroneProgram.LANNISTER))
             || (house.equals(ThroneProgram.STARK))
             || (house.equals(ThroneProgram.TYRELL)));
        }
    }
    
    @Test
    public void testLoadTourSchedule() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("14d1a2a8-e4d3-4348-9efc-65b99deedc78","{\"schedule\":{\"IL\":[\"Chicago\",\"Naperville\",\"Rockford\"],\"TX\":[\"Austin\",\"Houston\",\"Lubbock\"],\"WI\":[\"Beloit\",\"Madison\",\"Milwaukee\"]}}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        TourSchedule tourSchedule = arya.load(UUID.fromString("14d1a2a8-e4d3-4348-9efc-65b99deedc78"), TourSchedule.class);
        assertNotNull(tourSchedule);
        assertTrue(tourSchedule instanceof TourSchedule);
        Map<String, List<String>> schedule = tourSchedule.getSchedule();
        assertNotNull(schedule);
        assertEquals(3, schedule.keySet().size());
        for(String key : schedule.keySet()) {
            List<String> cities = schedule.get(key);
            assertNotNull(cities);
            assertEquals(3, cities.size());
        }
    }
    
    @Test
    public void testLoadCityInfo() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("67b750ac-17cc-448b-a627-df61a2b9095a","{\"infodex\":{\"IL\":{\"Rockford\":{\"temperature\":\"23\",\"population\":\"321234\"},\"Chicago\":{\"temperature\":\"45\",\"population\":\"123456\"},\"Naperville\":{\"temperature\":\"32\",\"population\":\"234567\"}},\"TX\":{\"Houston\":{\"temperature\":\"23\",\"population\":\"321234\"},\"Austin\":{\"temperature\":\"45\",\"population\":\"123456\"},\"Lubbock\":{}},\"WI\":{\"Madison\":{\"temperature\":\"32\",\"population\":\"234567\"},\"Beloit\":{\"temperature\":\"45\",\"population\":\"123456\"},\"Milwuakee\":{\"temperature\":\"23\",\"population\":\"321234\"}}}}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        CityInfo cityInfo = arya.load(UUID.fromString("67b750ac-17cc-448b-a627-df61a2b9095a"), CityInfo.class);
        assertNotNull(cityInfo);
        assertTrue(cityInfo instanceof CityInfo);
        Map<String, Map<String, Map<String, String>>> infodex = cityInfo.getInfodex();
        assertNotNull(infodex);
        assertEquals(3, infodex.keySet().size());
        Map<String, Map<String, String>> WI = infodex.get("WI");
        assertNotNull(WI);
        Map<String, String> beloit = WI.get("Beloit");
        assertNotNull(beloit);
        assertEquals("45", beloit.get("temperature"));
        assertEquals("123456", beloit.get("population"));
    }
    
    @Test
    public void testLoadThroneProgram2() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("428eba90-b399-4fe5-a40b-dca0d96277e0","{\"houses\":[[\"Eddard\",\"Catelyn\",\"Robb\",\"Sansa\",\"Arya\",\"Brandon\",\"Rickon\"],[\"Balon\",\"Asha\",\"Theon\"],[\"Mace\",\"Margaery\",\"Loras\"],[\"Tywin\",\"Cersei\",\"Jamie\",\"Tyrion\",\"Jeoffery\",\"Tommen\",\"Myrcella\"]]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        ThroneProgram2 throneProgram = arya.load(UUID.fromString("428eba90-b399-4fe5-a40b-dca0d96277e0"), ThroneProgram2.class);
        assertNotNull(throneProgram);
        assertTrue(throneProgram instanceof ThroneProgram2);
        List<List<String>> houses = throneProgram.getHouses();
        assertNotNull(houses);
        assertEquals(4, houses.size());
        for(List<String> house : houses) {
            assertNotNull(house);
        }
    }

    // ------------------------------------------------------------------------
    
    @Test
    public void testSaveTopTen() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(SongInfo.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(10);
        mockOutput.output(isA(TopTen.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        TopTen topTen = new TopTen();
        arya.save(topTen);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadTopTen() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("8b191bf5-c919-49c0-96c5-cf9dcfb03bda","{\"uuid\":\"8b191bf5-c919-49c0-96c5-cf9dcfb03bda\",\"title\":\"Harlem Shake\",\"artist\":\"Baauer\",\"weeksOnChart\":2}")
                    .put("cd8c2c58-a172-4a8b-b01f-1fd90979bab3","{\"uuid\":\"cd8c2c58-a172-4a8b-b01f-1fd90979bab3\",\"title\":\"Thrift Shop\",\"artist\":\"Macklemore \u0026 Ryan Lewis\",\"weeksOnChart\":21}")
                    .put("66c82dc6-83fa-428e-92fa-ae454e323e75","{\"uuid\":\"66c82dc6-83fa-428e-92fa-ae454e323e75\",\"title\":\"When I Was Your Man\",\"artist\":\"Bruno Mars\",\"weeksOnChart\":10}")
                    .put("bc2e4092-7f30-40dc-b298-8adfa287a46d","{\"uuid\":\"bc2e4092-7f30-40dc-b298-8adfa287a46d\",\"title\":\"I Knew You Were Trouble\",\"artist\":\"Taylor Swift\",\"weeksOnChart\":19}")
                    .put("e68cc40c-a7bd-42f4-81fe-6d5362af2b68","{\"uuid\":\"e68cc40c-a7bd-42f4-81fe-6d5362af2b68\",\"title\":\"Scream \u0026 Shout\",\"artist\":\"will.i.am \u0026 Britney Spears\",\"weeksOnChart\":13}")
                    .put("7ceb217c-42cc-4bc6-97b8-dbdd7ed2e01b","{\"uuid\":\"7ceb217c-42cc-4bc6-97b8-dbdd7ed2e01b\",\"title\":\"Started From The Bottom\",\"artist\":\"Drake\",\"weeksOnChart\":3}")
                    .put("9a58dedb-1367-4d8e-85ee-1b7d1b97bfe9","{\"uuid\":\"9a58dedb-1367-4d8e-85ee-1b7d1b97bfe9\",\"title\":\"Stay\",\"artist\":\"Rihanna\",\"weeksOnChart\":3}")
                    .put("1d4866fb-564d-4eb5-b819-3bf348395c36","{\"uuid\":\"1d4866fb-564d-4eb5-b819-3bf348395c36\",\"title\":\"The 20/20 Experience\",\"artist\":\"Justin Timberlake\",\"weeksOnChart\":7}")
                    .put("acd6c5e9-a492-4ab7-ab97-dc8bcb536836","{\"uuid\":\"acd6c5e9-a492-4ab7-ab97-dc8bcb536836\",\"title\":\"Locked Out Of Heaven\",\"artist\":\"Bruno Mars\",\"weeksOnChart\":21}")
                    .put("4dc62403-0b43-4381-be1c-d4482a141a23","{\"uuid\":\"4dc62403-0b43-4381-be1c-d4482a141a23\",\"title\":\"Love Me\",\"artist\":\"Lil Wayne\",\"weeksOnChart\":6}")
                    .put("7685da3e-00b4-49e1-83dd-5a26a74f1a94","{\"billboard\":{\"1\":\"8b191bf5-c919-49c0-96c5-cf9dcfb03bda\",\"2\":\"cd8c2c58-a172-4a8b-b01f-1fd90979bab3\",\"3\":\"66c82dc6-83fa-428e-92fa-ae454e323e75\",\"4\":\"bc2e4092-7f30-40dc-b298-8adfa287a46d\",\"5\":\"e68cc40c-a7bd-42f4-81fe-6d5362af2b68\",\"6\":\"7ceb217c-42cc-4bc6-97b8-dbdd7ed2e01b\",\"7\":\"9a58dedb-1367-4d8e-85ee-1b7d1b97bfe9\",\"8\":\"1d4866fb-564d-4eb5-b819-3bf348395c36\",\"9\":\"acd6c5e9-a492-4ab7-ab97-dc8bcb536836\",\"10\":\"4dc62403-0b43-4381-be1c-d4482a141a23\"}}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        TopTen topTen = arya.load(UUID.fromString("7685da3e-00b4-49e1-83dd-5a26a74f1a94"), TopTen.class);
        assertNotNull(topTen);
        assertTrue(topTen instanceof TopTen);
        Map<Integer,SongInfo> billboard = topTen.getBillboard();
        assertNotNull(billboard);
        assertEquals(10, billboard.keySet().size());
        for(Integer position : billboard.keySet()) {
            assertNotNull(position);
            assertTrue(position >= 1);
            assertTrue(position <= 10);
            assertNotNull(billboard.get(position));
        }
        SongInfo number5 = billboard.get(5);
        assertEquals("will.i.am & Britney Spears", number5.getArtist());
    }

    // ------------------------------------------------------------------------
    
    @Test
    public void testSaveGameOfArrays() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(GameOfArrays.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        GameOfArrays gameOfArrays = new GameOfArrays();
        arya.save(gameOfArrays);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadGameOfArrays() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("611c8942-a0c1-4dda-b185-0e42c7a6a39e","{\"b1\":[true,false,true],\"b2\":[true,false,true],\"b3\":[127,-128,35,-22],\"b4\":[127,-128,35,-22],\"c1\":[\"a\",\"b\",\"c\"],\"c2\":[\"a\",\"b\",\"c\"],\"s1\":[100,200,300,-31000],\"s2\":[100,200,300,-31000],\"i1\":[50,150,250,-350,-500000],\"i2\":[50,150,250,-350,-500000],\"l1\":[50,150,250,-350,-10000000000000],\"l2\":[50,150,250,-350,-10000000000000],\"f1\":[0.25,0.5,5000.0,235000.0],\"f2\":[0.25,0.5,5000.0,235000.0],\"d1\":[1000.25,1000.5,1.0005E7,1.000235E9],\"d2\":[1000.25,1000.5,1.0005E7,1.000235E9],\"s3\":[\"Win\",\"Or\",\"Die\"],\"d3\":[1362547312705,1362547312705,1362547312705],\"u1\":[\"429af1fa-eae6-48c5-b226-3cb91b00ec37\",\"223fea63-dca1-4ac4-ba60-5499bb489a6a\",\"36ebc1f3-de1f-4e54-99e5-5bd6f328844f\"]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        GameOfArrays gameOfArrays = arya.load(UUID.fromString("611c8942-a0c1-4dda-b185-0e42c7a6a39e"), GameOfArrays.class);
        assertNotNull(gameOfArrays);
        assertTrue(gameOfArrays instanceof GameOfArrays);
    }
    
    @Test
    public void testSaveGameOfLists() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(GameOfLists.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        GameOfLists gameOfLists = new GameOfLists();
        arya.save(gameOfLists);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadGameOfLists() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("ae87320d-33f2-4061-b6cb-b5a8493042f9","{\"b1\":[true,false,true],\"b4\":[127,-128,35,-22],\"c2\":[\"a\",\"b\",\"c\"],\"s2\":[100,200,300,-31000],\"i2\":[50,150,250,-350,-500000],\"l2\":[50,150,250,-350,-10000000000000],\"f2\":[0.25,0.5,5000.0,235000.0],\"d2\":[1000.25,1000.5,1.0005E7,1.000235E9],\"s3\":[\"Win\",\"Or\",\"Die\"],\"d3\":[1362548377949,1362548377949,1362548377949],\"u1\":[\"468f4f3b-ea93-4275-8172-61d28d8456f0\",\"562542b3-21b6-4745-8ecc-89ffa1f84109\",\"a42d8bad-9076-4de0-947f-bc81b7af676c\"]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        GameOfLists gameOfLists = arya.load(UUID.fromString("ae87320d-33f2-4061-b6cb-b5a8493042f9"), GameOfLists.class);
        assertNotNull(gameOfLists);
        assertTrue(gameOfLists instanceof GameOfLists);
    }
    
    @Test
    public void testSaveGameOfMaps() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(GameOfMaps.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        GameOfMaps gameOfMaps = new GameOfMaps();
        arya.save(gameOfMaps);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadGameOfMaps() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("0d8f3656-8554-40d9-a158-fedc34f75085","{\"b2\":{\"false\":\"falsey\",\"true\":\"truey\"},\"b4\":{\"127\":\"seven-eff\",\"-128\":\"eight-zero\",\"35\":\"two-three\",\"-22\":\"eee-a\"},\"c2\":{\"a\":\"a\",\"b\":\"bee\",\"c\":\"sea\"},\"s2\":{\"100\":\"one-hundred\",\"200\":\"two-hundred\",\"300\":\"three-hundred\",\"-31000\":\"negative thirty-one thousand\"},\"i2\":{\"50\":\"fifty\",\"150\":\"one-hundred and fifty\",\"250\":\"two-hundred and fifty\",\"-350\":\"negative three-hundred and fifty\"},\"l2\":{\"50\":\"fifty\",\"150\":\"one-hundred and fifty\",\"250\":\"two-hundred and fifty\",\"-350\":\"negative three-hundred and fifty\",\"-10000000000000\":\"a buttload\"},\"f2\":{\"0.25\":\"point two five\",\"0.5\":\"point five oh\",\"5000.0\":\"five thousand point oh\",\"235000.0\":\"two-hundred and thirty-five thousand point oh\"},\"d2\":{\"1000.25\":\"damn i\u0027m bored\",\"1000.5\":\"really really bored\",\"1.0005E7\":\"yep still bored\",\"1.000235E9\":\"only three left to go\"},\"s3\":{\"Win\":\"When you play the Game Of Thrones\",\"Or\":\"You Win or You Die\",\"Die\":\"There is no middle ground\"},\"d3\":{\"1362551085238\":\"before\",\"1362551085338\":\"during\",\"1362551085738\":\"after\"},\"u1\":{\"c9b71df3-4d43-4807-a96e-e1cf828fd94c\":\"reference #1\",\"cb45ea10-fec2-47f5-829a-cf9c39aea05c\":\"reference #2\",\"505e19ff-0b29-4a02-bd2a-a5eea7c5342e\":\"reference #3\"},\"b1\":{\"falsey\":false,\"truey\":true},\"b3\":{\"seven-eff\":127,\"eight-zero\":-128,\"two-three\":35,\"eee-a\":-22},\"c1\":{\"a\":\"a\",\"bee\":\"b\",\"sea\":\"c\"},\"s1\":{\"one-hundred\":100,\"two-hundred\":200,\"three-hundred\":300,\"negative thirty-one thousand\":-31000},\"i1\":{\"fifty\":50,\"one-hundred and fifty\":150,\"two-hundred and fifty\":250,\"negative three-hundred and fifty\":-350},\"l1\":{\"fifty\":50,\"one-hundred and fifty\":150,\"two-hundred and fifty\":250,\"negative three-hundred and fifty\":-350,\"a buttload\":-10000000000000},\"f1\":{\"point two five\":0.25,\"point five oh\":0.5,\"five thousand point oh\":5000.0,\"two-hundred and thirty-five thousand point oh\":235000.0},\"d1\":{\"damn i\u0027m bored\":1000.25,\"really really bored\":1000.5,\"yep still bored\":1.0005E7,\"only three left to go\":1.000235E9},\"s4\":{\"Win\":\"When you play the Game Of Thrones\",\"Or\":\"You Win or You Die\",\"Die\":\"There is no middle ground\"},\"d4\":{\"before\":1362551085239,\"during\":1362551085339,\"after\":1362551085739},\"u2\":{\"reference #1\":\"90c9af3c-b4ac-4b4f-8f1a-aa9fd25f08d8\",\"reference #2\":\"bc0998dd-b126-45a2-b2aa-7a882e3738d9\",\"reference #3\":\"d1307dc5-1359-47dc-ae37-52924ff796e5\"}}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        GameOfMaps gameOfMaps = arya.load(UUID.fromString("0d8f3656-8554-40d9-a158-fedc34f75085"), GameOfMaps.class);
        assertNotNull(gameOfMaps);
        assertTrue(gameOfMaps instanceof GameOfMaps);
    }
    
    @Test
    public void testSaveThroneProgram3() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(ThroneProgram3.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        ThroneProgram3 throneProgram3 = new ThroneProgram3();
        arya.save(throneProgram3);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadThroneProgram3() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("1d23abe6-90c2-46f0-be1e-e9eeed791309","{\"houses\":[{\"Robb\":\"Stark\",\"Catelyn\":\"Stark\",\"Brandon\":\"Stark\",\"Rickon\":\"Stark\",\"Sansa\":\"Stark\",\"Eddard\":\"Stark\",\"Arya\":\"Stark\"},{\"Balon\":\"Greyjoy\",\"Theon\":\"Greyjoy\",\"Asha\":\"Greyjoy\"},{\"Mace\":\"Tyrell\",\"Margaery\":\"Tyrell\",\"Loras\":\"Tyrell\"},{\"Tywin\":\"Lannister\",\"Tommen\":\"Lannister\",\"Myrcella\":\"Lannister\",\"Tyrion\":\"Lannister\",\"Jeoffery\":\"Lannister\",\"Jamie\":\"Lannister\",\"Cersei\":\"Lannister\"}]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        ThroneProgram3 throneProgram3 = arya.load(UUID.fromString("1d23abe6-90c2-46f0-be1e-e9eeed791309"), ThroneProgram3.class);
        assertNotNull(throneProgram3);
        assertTrue(throneProgram3 instanceof ThroneProgram3);
    }
    
    @Test
    public void testSavePlayer() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(Player.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(5);
        
        replay(mockOutput);
       
        Player alice = new Player(); alice.setName("Alice"); alice.setGender(Gender.FEMALE);
        Player bob = new Player(); bob.setName("Bob"); bob.setGender(Gender.MALE);
        Player carol = new Player(); carol.setName("Carol"); carol.setGender(Gender.FEMALE);
        Player dave = new Player(); dave.setName("Dave"); dave.setGender(Gender.MALE);
        Player eve = new Player(); eve.setName("Eve"); eve.setGender(Gender.FEMALE);

        List<Player> bobList = new ArrayList();
        bobList.add(alice);
        bobList.add(carol);
        bobList.add(eve);

        List<Player> carolList = new ArrayList();
        carolList.add(dave);
        carolList.add(bob);
        
        Map<Player,List<Player>> known = alice.getKnownAssociates();
        known.put(bob, bobList);
        known.put(carol, carolList);
        
        arya.save(alice);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadPlayer() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("09eb1dbb-27c4-4992-bf0c-1636ad6fc10d","{\"knownAssociates\":{},\"name\":\"Bob\",\"dateCreated\":1362798015043,\"gender\":\"MALE\"}")
                    .put("863c6792-5c15-47e1-863d-134a6d2601cf","{\"knownAssociates\":{},\"name\":\"Carol\",\"dateCreated\":1362798015043,\"gender\":\"FEMALE\"}")
                    .put("07cd357b-d0a1-48f3-8892-bed9415bac98","{\"knownAssociates\":{},\"name\":\"Eve\",\"dateCreated\":1362798015043,\"gender\":\"FEMALE\"}")
                    .put("89d2b3c7-ffd9-450f-b45a-ea33243d7431","{\"knownAssociates\":{},\"name\":\"Dave\",\"dateCreated\":1362798015043,\"gender\":\"MALE\"}")
                    .put("e435298d-4ae7-4440-ba02-19e517de1659","{\"knownAssociates\":{\"09eb1dbb-27c4-4992-bf0c-1636ad6fc10d\":[\"e435298d-4ae7-4440-ba02-19e517de1659\",\"863c6792-5c15-47e1-863d-134a6d2601cf\",\"07cd357b-d0a1-48f3-8892-bed9415bac98\"],\"863c6792-5c15-47e1-863d-134a6d2601cf\":[\"89d2b3c7-ffd9-450f-b45a-ea33243d7431\",\"09eb1dbb-27c4-4992-bf0c-1636ad6fc10d\"]},\"name\":\"Alice\",\"dateCreated\":1362798015043,\"gender\":\"FEMALE\"}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        Player alice = arya.load(UUID.fromString("e435298d-4ae7-4440-ba02-19e517de1659"), Player.class);
        assertNotNull(alice);
        assertTrue(alice instanceof Player);

        assertEquals("Alice", alice.getName());
        
        Map<Player,List<Player>> known = alice.getKnownAssociates();
        assertEquals(2, known.keySet().size());

        for(Player knownPlayer : known.keySet()) {
            List<Player> associates = known.get(knownPlayer);
            assertNotNull(associates);
            assertTrue(associates.size() >= 2);
        }
        
        Player bob = arya.load(UUID.fromString("09eb1dbb-27c4-4992-bf0c-1636ad6fc10d"), Player.class);
        assertNotNull(bob);
        assertTrue(bob instanceof Player);
        assertEquals(Gender.MALE, bob.getGender());
    }
    
    @Test
    public void testSaveConcreteTypes() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(ConcreteTypes.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        arya.save(new ConcreteTypes());
        verify(mockOutput);
    }
    
    @Test
    public void testLoadConcreteTypes() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("9a07d79f-e645-4fc5-93e7-f49c7c9b7134","{\"intList\":[2,4,6],\"intArrayList\":[2,4,6],\"intLinkedList\":[2,4,6],\"intSet\":[2,4,6],\"intSortedSet\":[2,4,6],\"intHashSet\":[2,4,6],\"intTreeSet\":[2,4,6],\"intMap\":{\"two\":2,\"four\":4,\"six\":6},\"intNavigableMap\":{\"four\":4,\"six\":6,\"two\":2},\"intSortedMap\":{\"four\":4,\"six\":6,\"two\":2},\"intHashMap\":{\"two\":2,\"four\":4,\"six\":6},\"intTreeMap\":{\"four\":4,\"six\":6,\"two\":2}}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        ConcreteTypes concreteTypes = arya.load(UUID.fromString("9a07d79f-e645-4fc5-93e7-f49c7c9b7134"), ConcreteTypes.class);
        assertNotNull(concreteTypes);
        assertTrue(concreteTypes instanceof ConcreteTypes);
    }
    
    @Test
    public void testDerivedTypes() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(MagicSword.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
        
        MagicSword magicSword = new MagicSword();
        
        magicSword.setUuid(UUID.fromString("de626224-a0b1-47e3-904d-a243eb4ca6b0"));
        
        magicSword.setLongDesc("This magical sword belonged to Thoros of Myr");
        magicSword.setShortDesc("a magical sword");
        
        magicSword.setDmgBonus(5);
        magicSword.setEpicName("Lightbringer");
        magicSword.setSpecialDesc("This magical sword is wreathed in flames");
        magicSword.setToHitBonus(10);
        
        arya.save(magicSword);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadDerivedTypes() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("de626224-a0b1-47e3-904d-a243eb4ca6b0","{\"toHitBonus\":10,\"dmgBonus\":5,\"specialDesc\":\"This magical sword is wreathed in flames\",\"epicName\":\"Lightbringer\",\"shortDesc\":\"a magical sword\",\"longDesc\":\"This magical sword belonged to Thoros of Myr\",\"uuid\":\"de626224-a0b1-47e3-904d-a243eb4ca6b0\"}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        MagicSword magicSword = arya.load(UUID.fromString("de626224-a0b1-47e3-904d-a243eb4ca6b0"), MagicSword.class);
        assertNotNull(magicSword);
        assertTrue(magicSword instanceof MagicSword);

        assertEquals("de626224-a0b1-47e3-904d-a243eb4ca6b0", magicSword.getUuid().toString());

        assertEquals("This magical sword belonged to Thoros of Myr", magicSword.getLongDesc());
        assertEquals("a magical sword", magicSword.getShortDesc());

        assertEquals(5, magicSword.getDmgBonus());
        assertEquals("Lightbringer", magicSword.getEpicName());
        assertEquals("This magical sword is wreathed in flames", magicSword.getSpecialDesc());
        assertEquals(10, magicSword.getToHitBonus());
    }
    
    @Test
    public void testSaveArmedPlayer() {
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(Player.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        mockOutput.output(isA(MagicSword.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);
       
        Player alice = new Player(); alice.setName("Alice"); alice.setGender(Gender.FEMALE);

        MagicSword magicSword = new MagicSword();
        
        magicSword.setUuid(UUID.fromString("de626224-a0b1-47e3-904d-a243eb4ca6b0"));
        
        magicSword.setLongDesc("This magical sword belonged to Thoros of Myr");
        magicSword.setShortDesc("a magical sword");
        
        magicSword.setDmgBonus(5);
        magicSword.setEpicName("Lightbringer");
        magicSword.setSpecialDesc("This magical sword is wreathed in flames");
        magicSword.setToHitBonus(10);
        
        alice.getWeapons().add(magicSword);
        
        arya.save(alice);
        verify(mockOutput);
    }
    
    @Test
    public void testLoadArmedPlayer() {
        AryaInput mapInput = new AryaInput() {
            final Map<String,String> inputMap =
                new ImmutableMap.Builder<String,String>()
                    .put("de626224-a0b1-47e3-904d-a243eb4ca6b0","{\"_Arya_TYPE_\":\"com.pmeade.arya.domain.MagicSword\",\"toHitBonus\":10,\"dmgBonus\":5,\"specialDesc\":\"This magical sword is wreathed in flames\",\"epicName\":\"Lightbringer\",\"shortDesc\":\"a magical sword\",\"longDesc\":\"This magical sword belonged to Thoros of Myr\",\"uuid\":\"de626224-a0b1-47e3-904d-a243eb4ca6b0\"}")
                    .put("04f100fc-d713-4f0f-b9f4-a58693f5047f","{\"_Arya_TYPE_\":\"com.pmeade.arya.domain.Player\",\"knownAssociates\":{},\"name\":\"Alice\",\"dateCreated\":1363078747371,\"gender\":\"FEMALE\",\"weapons\":[\"de626224-a0b1-47e3-904d-a243eb4ca6b0\"]}")
                    .build();
            public <T> String input(UUID uuid, Class<T> type) {
                return inputMap.get(uuid.toString());
            }
        };
        arya.register(mapInput);
        
        Player alice = arya.load(UUID.fromString("04f100fc-d713-4f0f-b9f4-a58693f5047f"), Player.class);
        assertNotNull(alice);
        assertTrue(alice instanceof Player);

        assertEquals(Gender.FEMALE, alice.getGender());
        assertEquals("Alice", alice.getName());

        List<Weapon> weapons = alice.getWeapons();
        assertNotNull(weapons);
        assertEquals(1, weapons.size());
        
        Weapon weapon = weapons.get(0);
        assertNotNull(weapon);
        assertTrue(weapon instanceof MagicSword);
        
        MagicSword magicSword = (MagicSword) weapon;
        assertEquals("de626224-a0b1-47e3-904d-a243eb4ca6b0", magicSword.getUuid().toString());

        assertEquals("This magical sword belonged to Thoros of Myr", magicSword.getLongDesc());
        assertEquals("a magical sword", magicSword.getShortDesc());

        assertEquals(5, magicSword.getDmgBonus());
        assertEquals("Lightbringer", magicSword.getEpicName());
        assertEquals("This magical sword is wreathed in flames", magicSword.getSpecialDesc());
        assertEquals(10, magicSword.getToHitBonus());
    }
    
//    @Test
    public void testSaveIncompleteBooleanArray() {
//        arya.register(new AryaOutput() {
//            public <T> void output(T t, UUID uuid, String json) {
//                StringBuilder sb = new StringBuilder(".put(\"");
//                sb.append(uuid.toString());
//                sb.append("\",\"");
//                sb.append(json.replace("\"", "\\\""));
//                sb.append("\")");
//                System.err.println(sb.toString());
//            }
//        });
        
        AryaOutput mockOutput = createMock(AryaOutput.class);
        arya.register(mockOutput);
        
        mockOutput.output(isA(MissingBooleans.class), isA(UUID.class), isA(String.class));
        expectLastCall().times(1);
        
        replay(mockOutput);

        MissingBooleans missingBooleans = new MissingBooleans();
        Boolean[] booleans = new Boolean[5];
        booleans[0] = Boolean.FALSE;
        booleans[1] = null;
        booleans[2] = Boolean.TRUE;
        booleans[3] = null;
        booleans[4] = Boolean.FALSE;
        missingBooleans.setBooleans(booleans);
        
        arya.save(missingBooleans);
        
        verify(mockOutput);
    }
}
